package comments

import com.mongodb.casbah.Imports._

case class Comment(url: String, title: String, body: String)

case class CommentsRepository(collection: MongoCollection) {

  def toComment(db: DBObject): Option[Comment] = for {
    u <- db.getAs[String]("url")
    t <- db.getAs[String]("title")
    b <- db.getAs[String]("body")
  } yield Comment(u, t, b)

  def create(url: String, title: String, body: String) {
    collection += MongoDBObject("url" -> url, "title" -> title, "body" -> body)
  }

  def findByUrl(url: String): List[Comment] = {
    collection.find(MongoDBObject("url" -> url)).toList flatMap toComment
  }

  def findAll: List[Comment] = {
    collection.find.toList flatMap toComment
  }

}
