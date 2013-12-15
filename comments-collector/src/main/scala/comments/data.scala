package comments

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId

case class Comment(url: String, title: String, body: String)

case class CommentsRepository(collection: MongoCollection) {

  def create(url: String, title: String, body: String): ObjectId = {
    val m = MongoDBObject("url" -> url, "title" -> title, "body" -> body)
    collection += m
    m.getAs[ObjectId]("_id").get
  }

  def findAll: List[Comment] = {
    collection.find.toList flatMap toComment
  }

  def findByUrl(url: String): List[Comment] = {
    collection.find(MongoDBObject("url" -> url)).toList flatMap toComment
  }

  protected def toComment(db: DBObject): Option[Comment] = for {
    u <- db.getAs[String]("url")
    t <- db.getAs[String]("title")
    b <- db.getAs[String]("body")
  } yield Comment(u, t, b)

}
