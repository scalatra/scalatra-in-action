package comments

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId

case class Comment(id: String, url: String, title: String, body: String)

case class CommentsRepository(collection: MongoCollection) {

  def create(url: String, title: String, body: String): String = {
    val m = MongoDBObject("url" -> url, "title" -> title, "body" -> body)
    collection += m
    m.getAs[ObjectId]("_id").get.toString
  }

  def findAll: List[Comment] = {
    collection.find.toList flatMap toComment
  }

  def findByUrl(url: String): List[Comment] = {
    collection.find(MongoDBObject("url" -> url)).toList flatMap toComment
  }

  protected def toComment(db: DBObject): Option[Comment] = for {
    id <- db.getAs[ObjectId]("_id")
    u <- db.getAs[String]("url")
    t <- db.getAs[String]("title")
    b <- db.getAs[String]("body")
  } yield Comment(id.toString, u, t, b)

}
