package comments

import org.scalatra._
import org.scalatra.scalate.ScalateSupport

import org.scalatra.swagger._

import com.mongodb.casbah.Imports._

import com.fasterxml.jackson.databind._
import org.json4s.jackson.Json4sScalaModule
import org.json4s.{DefaultFormats, Formats}

case class Comment(url: String, title: String, body: String)

class CommentsApiDoc(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase

class CommentsFrontend(mongoColl: MongoCollection) extends CommentsCollectorStack {

  get("/") {
    "frontend"
  }

}

class CommentsApi(mongoColl: MongoCollection)(implicit val swagger: Swagger) extends ScalatraServlet with MongoDbJsonConversion with SwaggerSupport {

  // identify the application to swagger
  override protected val applicationName = Some("comments-collector")
  protected val applicationDescription = "The comments API. It exposes operations for adding comments and retrieving lists of comments."


  // An API description about retrieving comments
  val getComments = (apiOperation[List[Comment]]("getComments")
    summary ("Show all comments")
    notes ("""Shows all the available comments. You can optionally search
     it using a query string parameter such as url=news.intranet.""")
    parameters (
      Parameter("url", """A full or partial URL with which to filter the
            result set, e.g. menu.intranet""", DataType.String,
        paramType = ParamType.Query, required = false)
      ))

  // An API description about adding a comment
  val addComment = (apiOperation[Unit]("addComment")
    summary("Add a comment")
    notes("Allows clients to add a new comment")
    nickname("addComment")
    parameters(
      Parameter("url", "The full URL to the resource you'd like to add",
        DataType.String, paramType = ParamType.Body, required = true),
      Parameter("title", "The title of the comment", DataType.String,
        paramType = ParamType.Body, required = true),
      Parameter("body", "The main information of the comment",
        DataType.String, paramType = ParamType.Body, required = true)
      ))


  /*
   * Retrieve a list of comments
   */
  get("/", operation(getComments)) {
    def toComment(db: DBObject): Option[Comment] = for {
      u <- db.getAs[String]("url")
      s <- db.getAs[String]("string")
      t <- db.getAs[String]("title")
    } yield Comment(u, s, t)

    val list = params.get("url") match {
      case Some(url) =>

        mongoColl.findOne(MongoDBObject("url" -> url)) match {
          case Some(x) => List(x)
          case None => halt(404)
        }

      case None => mongoColl.find
    }

    list flatMap toComment
  }


  /*
   * Adds a new comment to the list of available comments
   */
  post("/", operation(addComment)) {
    for {
      url <- params.get("url")
      title <- params.get("title")
      body <- params.get("body")
    } {
      mongoColl += MongoDBObject("url" -> url, "title" -> title, "body" -> body)
    }
  }

}
