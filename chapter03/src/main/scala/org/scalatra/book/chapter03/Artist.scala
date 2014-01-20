package org.scalatra.book.chapter03

import scala.xml.Node
import scala.collection.concurrent.TrieMap

case class Artist(name: String, nationality: String, isActive: Boolean) {
  def toXml: Node =
    <artist>
      <name>${name}</name>
      <nationality>${nationality}</nationality>
      <is-active>${isActive}</is-active>
    </artist>

  def toJson: String = ??? // Left as an exercise to the JSON chapter.
}

object Artist {
  // URLs are cleaner without spaces.
  def fromParam(name: String) = name.replace('_', ' ')

  private val db = TrieMap.empty[String, Artist]

  def find(name: String): Option[Artist] = db.get(fromParam(name))

  def fetchAll(): List[Artist] = db.values.toList

  def exists(name: String): Boolean = db.contains(fromParam(name))

  def save(artist: Artist): Option[Artist] = db.put(artist.name, artist)

  def update(artist: Artist): Option[Artist] = db.put(artist.name, artist)

  def delete(name: String): Option[Artist] = db.remove(fromParam(name))
}