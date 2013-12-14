package comments

import org.scalatra._
import scalate.ScalateSupport

import org.scalatra.swagger._

import com.fasterxml.jackson.databind._
import org.json4s.jackson.Json4sScalaModule
import org.json4s.{DefaultFormats, Formats}

class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase {
  override protected implicit val jsonFormats: Formats = DefaultFormats
}

class CommentsServlet(implicit val swagger: Swagger) extends CommentsCollectorStack {

  override protected val applicationName = Some("comments-collector")
  protected val applicationDescription = "The comments API. It exposes operations for adding comments and retrieving lists of comments."

  get("/",
    summary("Show all comments"),
    notes("Shows all the available comments. You can optionally search it using a query string parameter such as url=news.intranet."),
    nickname("get comments"),
    responseClass("List[Comment]"),
    parameters(
    Parameter("url", "query", DataType.String, required = false))) {
  }

}
