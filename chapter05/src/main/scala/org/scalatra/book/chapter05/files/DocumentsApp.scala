package org.scalatra.book.chapter05.files

import org.scalatra.ScalatraServlet
import org.scalatra.servlet._
import java.io.{IOException, File}
import org.scalatra.servlet.MultipartConfig

class DocumentsApp extends ScalatraServlet with FileUploadSupport {

  val mediaBase = "/opt/media/documents"

  configureMultipartHandling(MultipartConfig(maxFileSize = Some(10 * 1024 * 1024)))

  get("/") {
    contentType = "text/html"
    <html>
      <head>
        <title>Upload a document</title>
      </head>
      <body>
        <div id="upload-form">
          <h2>Upload a document</h2>
          <form enctype="multipart/form-data" method="post">
          <input type="file" name="document" />
          <input type="hidden" name="coffee" value="babe" />
          <input type="submit" />
          </form>
        </div>
      </body>
    </html>
  }

  post("/") {
    val item: FileItem = fileParams.get("document").getOrElse(halt(500))
    val uuid = java.util.UUID.randomUUID()
    val targetFile = new File("%s/%s".format(mediaBase, uuid))

    // write the file to the disk
    item.write(targetFile)

    // return information about the file, will be rendered by the render pipeline
    <html>
      <head>
        <title>Upload successful</title>
      </head>
      <body>
        <p>Name: {item.name}</p>
        <p>MIME Type: {item.contentType.getOrElse("unknown")}</p>
        <p>Size: {item.size}</p>
        <p>Saved to: {uuid}</p>
      </body>
    </html>
  }  

  get("""/([A-Za-z0-9\-]+)""".r) {
    val captures = multiParams("captures")
    val id = captures(0)

    val fileName = "%s/%s".format(mediaBase, id)
    val file = new File(fileName)
    if (file.exists) {
      contentType = "application/octet-stream"
      file
    } else {
      halt(404, "Cloud not find document: %s".format(id))
    }
  }

  delete("""/([A-Za-z0-9\-]+)""".r) {
    val captures = multiParams("captures")
    val id = captures(0)

    val fileName = "%s/%s".format(mediaBase, id)
    val file = new File(fileName)
    if (file.exists) {
      if (!file.delete) halt(404, "Could not delete document: %s".format(id))
    } else {
      halt(404, "Cloud not find document: %s".format(id))
    }
  }

  error {
    case e: SizeConstraintExceededException =>
      halt(500, "too much!", reason="too much!")
    case e: IOException =>
      halt(500, "Server denied me my meal, thanks anyway.", reason="unknown")
  }

}
