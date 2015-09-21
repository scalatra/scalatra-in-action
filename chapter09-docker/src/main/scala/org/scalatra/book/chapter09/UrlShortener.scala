package org.scalatra.book.chapter09

object UrlShortener {
  val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
  def randChar = chars(scala.util.Random.nextInt(chars.size))

  def nextFreeToken = (1 to 8).foldLeft("")((acc, _) => acc + randChar)
}
