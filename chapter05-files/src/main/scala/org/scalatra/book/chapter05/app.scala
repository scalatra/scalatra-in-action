package org.scalatra.book.chapter05

import java.io.{FileInputStream, File, IOException}

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.scalatra.servlet._

class DocumentsApp(store: DocumentStore) extends ScalatraServlet with FileUploadSupport with ScalateSupport {

  configureMultipartHandling(MultipartConfig(
    maxFileSize = Some(30 * 1024 * 1024),
    maxRequestSize = Some(100 * 1024 * 1024)
  ))

  // add a sample file for serving
  store.add("sample.jpg", new FileInputStream(new File("data/sample.jpg")), Some("application/jpeg"), "a round of minesweeper")

  // renders the user interface
  get("/") {
    contentType = "text/html"
    scaml("index.scaml", "files" -> store.list)
  }

  // create a new document
  post("/documents") {
    val file = fileParams.get("file") getOrElse halt(400, reason = "no file in request")
    val desc = params.get("description") getOrElse halt(400, reason = "no description given")
    val name = file.getName
    val in = file.getInputStream
    store.add(name, in, file.getContentType, desc)
    redirect("/")
  }

  // returns a specific documents
  get("/documents/:documentId") {
    val id = params.as[Long]("documentId")
    val doc = store.getDocument(id) getOrElse halt(404, reason = "could not find document")
    doc.contentType foreach { ct => contentType = ct }
    response.setHeader("Content-Disposition", f"""attachment; filename="${doc.name}"""")
    response.setHeader("Content-Description", doc.description)
    // response.setHeader("Content-Disposition", f"""inline; filename="${doc.name}"""")
    // response.setHeader("Content-Disposition", f"""inline"""")
    store.getFile(id)
  }

  // create a new version -> bonus
  put("/documents/:documentId") {

  }

  error {
    case e: SizeConstraintExceededException =>
      halt(500, "too much!", reason="too much!")
    case e: IOException =>
      println(e.getMessage)
      e.printStackTrace()
      halt(500, "Server denied me my meal, thanks anyway.", reason="unknown")
  }

  // sample routes
  get("/sample") {
    // contentType = "image/jpeg"
    // response.setHeader("Content-Disposition", "attachment; filename=first_sample.jpg")
    new File("data/sample.jpg")
  }

  post("/sample") {
    val file = fileParams("sample")
    val desc = fileParams("description")
    <div>
      <h1>Received {file.getSize} bytes</h1>
      <p>Description: {desc}</p>
    </div>
  }
  // ---



}
