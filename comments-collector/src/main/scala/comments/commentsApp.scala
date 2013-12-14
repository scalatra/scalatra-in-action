package comments

import org.scalatra._
import org.scalatra.scalate.ScalateSupport

import org.scalatra.swagger._

import com.mongodb.casbah.Imports._

import com.fasterxml.jackson.databind._
import org.json4s.jackson.Json4sScalaModule
import org.json4s.{DefaultFormats, Formats}


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


  // routes and actions

  /*
   * Retrieve a list of comments
   */
  get("/",
    summary("Show all comments"),
    notes("Shows all the available comments. You can optionally search it using a query string parameter such as url=news.intranet."),
    nickname("get comments"),
    responseClass("List[Comment]"),
    parameters(
      Parameter("url", "query", DataType.String, required = false))) {

    params.get("url") match {
      case Some(url) =>

        mongoColl.findOne(MongoDBObject("url" -> url)) match {
          case Some(x) => x
          case None => halt(404)
        }

      case None => mongoColl.find
    }

  }


  /*
   * Adds a new comment to the list of available comments
   */
  post("/",
    summary("Add a comment"),
    notes("Allows clients to add a new comment"),
    nickname("addComment"),
    responseClass("Unit"),
    parameters(
      Parameter("url", "query", DataType.String),
      Parameter("title", "query", DataType.String),
      Parameter("body", "query", DataType.String))) {

    for {
      url <- params.get("url")
      title <- params.get("title")
      body <- params.get("body")
    } {
      mongoColl += MongoDBObject("url" -> url, "title" -> title, "body" -> body)
    }
  }

}
