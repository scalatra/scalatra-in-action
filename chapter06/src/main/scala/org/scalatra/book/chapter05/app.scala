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
  store.add("strategy.jpg", new FileInputStream(new File("data/strategy.jpg")), Some("application/jpeg"), "bulletproof business strategy")
  store.add("manual.pdf", new FileInputStream(new File("data/manual.pdf")), Some("application/pdf"), "the manual about foos")

  // renders the user interface
  get("/") {
    contentType = "text/html"
    scaml("index.scaml", "files" -> store.list)
  }

  // creates a new document
  post("/documents") {
    val file = fileParams.get("file") getOrElse halt(400, reason = "no file in request")
    val desc = params.get("description") getOrElse halt(400, reason = "no description given")
    val name = file.getName
    val in = file.getInputStream
    store.add(name, in, file.getContentType, desc)
    redirect("/")
  }

  // returns a specific document
  get("/documents/:documentId") {
    val id = params.as[Long]("documentId")
    val doc = store.getDocument(id) getOrElse halt(404, reason = "could not find document")
    doc.contentType foreach { ct => contentType = ct }
    response.setHeader("Content-Description", doc.description)
    response.setHeader("Content-Disposition", f"""inline; filename="${doc.name}"""")
    // response.setHeader("Content-Disposition", f"""attachment; filename="${doc.name}"""") // <-- use this if you want to trigger a download prompt in most browsers
    store.getFile(id)
  }

  // create a new version -> bonus
  put("/documents/:documentId") {

  }

  error {
    case e: SizeConstraintExceededException => halt(500, "Too much!")
    case e: IOException => halt(500, "Server denied me my meal, thanks anyway.")
  }

  get("/css/*") {
    serveStaticResource() getOrElse halt(404, <h1>Not found.</h1>)
  }

  notFound {
    contentType = null
    serveStaticResource() getOrElse halt(404, <h1>Not found.</h1>)
    halt(404, <h1>Not found.</h1>)
  }

  // sample routes
  get("/sample") {
    // contentType = "image/jpeg"
    // response.setHeader("Content-Disposition", "attachment; filename=first_sample.jpg")
    new File("data/strategy.jpg")
  }

  post("/sample") {
    val file = fileParams("sample")
    val desc = params("description")
    <div>
      <h1>Received {file.getSize} bytes</h1>
      <p>Description: {desc}</p>
    </div>
  }
  // ---



}
