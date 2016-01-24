package org.scalatra.book.chapter06

import java.io.{File, FileOutputStream, InputStream, OutputStream}
import java.util.concurrent.atomic.AtomicLong

case class Document(id: Long, name: String, contentType: Option[String], description: String)

// simple document store
// - an index of documents exists in-memory
// - the documents are saved as files on the filesystem
case class DocumentStore(base: String) {

  private val fileNameIndex = collection.concurrent.TrieMap[Long, Document]()

  private val idCounter = new AtomicLong(0)

  // adds a new document
  def add(name: String, in: InputStream, contentType: Option[String], description: String): Long = {
    val id = idCounter.getAndIncrement
    val out = new FileOutputStream(getFile(id))
    // Files.copy(in, out)
    copyStream(in, out)
    fileNameIndex(id) = Document(id, name, contentType, description)
    id
  }

  // a sequence of all documents
  def list: Seq[Document] = {
    fileNameIndex.values.toSeq
  }

  // a document for a given id
  def getDocument(id: Long): Option[Document] = {
    fileNameIndex.get(id)
  }

  // a file for a given id
  def getFile(id: Long): File = new File(f"$base/$id")

  // writes an input stream to an output stream
  private def copyStream(input: InputStream, output: OutputStream) {
    val buffer = Array.ofDim[Byte](1024)
    var bytesRead: Int = 0
    while (bytesRead != -1) {
      bytesRead = input.read(buffer)
      if (bytesRead > 0) output.write(buffer, 0, bytesRead)
    }
  }

}
