package org.scalatra.book.chapter05

import java.io.{File, FileOutputStream, IOException, InputStream, OutputStream}
import java.util.concurrent.atomic.AtomicLong

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.scalatra.servlet._

// file-system based document store with in-memory index
case class Document(id: Long, name: String, contentType: Option[String])

case class DocumentStore(base: String) {

  private val fileNameIndex = collection.concurrent.TrieMap[Long, Document]()

  private val idCounter = new AtomicLong(0)

  def add(name: String, in: InputStream, contentType: Option[String]): Long = {
    val id = idCounter.getAndIncrement
    val out = new FileOutputStream(toFile(id))
    // Files.copy(in, out)
    copyStream(in, out)
    fileNameIndex(id) = Document(id, name, contentType)
    id
  }

  def get(id: Long): Option[Document] = {
    fileNameIndex.get(id)
  }

  def toFile(id: Long): File = new File(f"$base/$id")

  def list: Map[Long, Document] = {
    fileNameIndex.toMap
  }

  def copyStream(input: InputStream, output: OutputStream) {
    val buffer = Array.ofDim[Byte](1024)
    var bytesRead: Int = 0
    while (bytesRead != -1) {
      bytesRead = input.read(buffer)
      if (bytesRead > 0) output.write(buffer, 0, bytesRead)
    }
  }

}

class DocumentsApp(store: DocumentStore) extends ScalatraServlet with FileUploadSupport with ScalateSupport {

  configureMultipartHandling(MultipartConfig(maxFileSize = Some(10 * 1024 * 1024)))

  get("/sample") {
    contentType = "image/jpeg"
    response.setHeader("Content-Disposition", "attachment; filename=first_sample.jpg")
    new File("data/sample.jpg")
  }

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

  // create a new version -> bonus
  put("/documents/:id") {

  }

  get("/documents/:id") {
    val id = params.as[Long]("id")
    store.get(id) match {
      case Some(doc) =>
        doc.contentType foreach { ct => contentType = ct }
        response.setHeader("Content-Disposition", f"""attachment; filename="${doc.name}"""")
        store.toFile(id)
      case None =>
        halt(404, reason = "could not find document")
    }
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
