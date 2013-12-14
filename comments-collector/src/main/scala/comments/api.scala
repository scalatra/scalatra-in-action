package comments

import org.scalatra._

import org.scalatra.swagger._

import org.scalatra.json._

import com.fasterxml.jackson.databind._
import org.json4s.jackson.Json4sScalaModule
import org.json4s.{DefaultFormats, Formats}

class CommentsApiDoc(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase {
  override protected implicit val jsonFormats: Formats = DefaultFormats
}

class CommentsApi(comments: CommentsRepository)(implicit val swagger: Swagger) extends ScalatraServlet with JacksonJsonSupport with JValueResult with SwaggerSupport {

  // Identifies the application to swagger
  override protected val applicationName = Some("comments-collector")
  protected val applicationDescription = "The comments API. It exposes operations for adding comments and retrieving lists of comments."

  // An API description about retrieving comments
  val getComments = (apiOperation[List[Comment]]("getComments")
    summary ("Show all comments")
    notes ("""Shows all the available comments. You can optionally search
     it using a query string parameter such as url=news.intranet.""")
    parameters (
      Parameter("url", DataType.String, Some("A full or partial URL with which to filter the result set"), Some("Notes go here"), ParamType.Query, None)
      ))

  // An API description about adding a comment
  val addComment = (apiOperation[Unit]("addComment")
    summary("Add a comment")
    notes("Allows clients to add a new comment")
    nickname("addComment")
    parameters(
      Parameter("url", DataType.String, Some("The full URL to the resource you'd like to add"), None, ParamType.Body, required = true),
      Parameter("title", DataType.String, Some("The title of the comment"), None, ParamType.Body, required = true),
      Parameter( "body", DataType.String, Some("The main information of the comment"), None, ParamType.Body, required = true)
      ))

  // Required to convert an instance of Comment to JSON text
  implicit val jsonFormats = DefaultFormats

  before("/*") {
    contentType = formats("json")
  }

  // Retrieves a list of comments
  get("/comments", operation(getComments)) {
    params.get("url") match {
      case Some(url) => comments.findByUrl(url)
      case None      => comments.findAll
    }
  }

  // Creates a new comment
  post("/comments", operation(addComment)) {
    for {
      url <- params.get("url")
      title <- params.get("title")
      body <- params.get("body")
    } {
      comments.create(url, title, body)
    }
  }

}
