package org.scalatra.book.chapter05

import java.io.{File, IOException}

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.scalatra.servlet._

class DocumentsApp(store: DocumentStore) extends ScalatraServlet with FileUploadSupport with ScalateSupport {

  configureMultipartHandling(MultipartConfig(
    maxFileSize = Some(10 * 1024 * 1024),
    maxRequestSize = Some(30 * 1024 * 1024),
  ))

  // a sample file route
  get("/sample") {
    // contentType = "image/jpeg"
    // response.setHeader("Content-Disposition", "attachment; filename=first_sample.jpg")
    new File("data/sample.jpg")
  }

  // renders the user interface
  get("/") {
    contentType = "text/html"
    scaml("index.scaml", "files" -> store.list)
  }

  // create a new document
  post("/documents") {
    fileParams.get("file") match {
      case Some(file) =>
        val name = file.getName
        val in = file.getInputStream
        store.add(name, in, file.getContentType)
        redirect("/")
      case None => halt(400, reason = "no file in request")
    }
  }

  // returns a specific documents
  get("/documents/:documentId") {
    val id = params.as[Long]("documentId")
    store.get(id) match {
      case Some(doc) =>
        doc.contentType foreach { ct => contentType = ct }
        response.setHeader("Content-Disposition", f"""attachment; filename="${doc.name}"""")
        store.toFile(id)
      case None =>
        halt(404, reason = "could not find document")
    }
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



}
