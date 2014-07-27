package org.scalatra.book.chapter05

import java.io.{OutputStream, File, FileOutputStream, InputStream}
import java.util.concurrent.atomic.AtomicLong

// file-system based document store with in-memory index
case class Document(id: Long, name: String, contentType: Option[String])

case class DocumentStore(base: String) {

  private val fileNameIndex = collection.concurrent.TrieMap[Long, Document]()

  private val idCounter = new AtomicLong(0)

  def add(name: String, in: InputStream, contentType: Option[String]): Long = {
    val id = idCounter.getAndIncrement
    val out = new FileOutputStream(getFile(id))
    // Files.copy(in, out)
    copyStream(in, out)
    fileNameIndex(id) = Document(id, name, contentType)
    id
  }

  def get(id: Long): Option[Document] = {
    fileNameIndex.get(id)
  }

  def getFile(id: Long): File = new File(f"$base/$id")

  def list: Map[Long, Document] = {
    fileNameIndex.toMap
  }

  private def copyStream(input: InputStream, output: OutputStream) {
    val buffer = Array.ofDim[Byte](1024)
    var bytesRead: Int = 0
    while (bytesRead != -1) {
      bytesRead = input.read(buffer)
      if (bytesRead > 0) output.write(buffer, 0, bytesRead)
    }
  }

}
