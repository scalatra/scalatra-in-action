package com.example.comments

import org.scalatra._
import com.example.comments.data.CommentData
import com.example.comments.models.Comment

// Swagger-specific Scalatra imports
import org.scalatra.swagger._

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._


class CommentsController(implicit val swagger: Swagger) extends ScalatraServlet
  with NativeJsonSupport with JValueResult with SwaggerSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  override protected val applicationName = Some("intranet-comments")
  protected val applicationDescription = "The comments API. It exposes operations for adding comments and retrieving lists of comments."


  before() {
    contentType = formats("json")
  }

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
    params.get("url") match {
      case Some(url) => CommentData.all filter (
        _.url.toLowerCase contains url.toLowerCase())
      case None => CommentData.all
    }
  }

  /*
   * Adds a new comment to the list of available comments
   */
  post("/", operation(addComment)) {
    val url: String = params.getOrElse("url", halt(400))
    val title: String = params.getOrElse("title", halt(400))
    val body: String = params.getOrElse("body", halt(400))
    val comment = Comment(url, title, body)
    CommentData.all = comment :: CommentData.all
  }

}
