package comments

import org.scalatra._
import org.scalatra.scalate.ScalateSupport
import java.net.URLEncoder

class CommentsFrontend(commentsRepo: CommentsRepository) extends ScalatraServlet with ScalateSupport {
  get("/") {
    val urls = commentsRepo.findAll.groupBy(_.url).keys.toSeq.sorted
    layoutTemplate("index", "title" -> "Articles with comments", "urls" -> urls)
  }

  get("/:url") {
    val url = params("url")
    val urls = commentsRepo.findAll.groupBy(_.url).keys.toSeq.sorted
    layoutTemplate("comments", "urls" -> urls, "url" -> url, "comments" -> commentsRepo.findByUrl(url))
  }

  post("/:url") {
    val url = params("url")
    val title = params.getOrElse("title", halt(BadRequest("title is required")))
    val body = params.getOrElse("body", halt(BadRequest("body is required")))
    commentsRepo.create(url, title, body)
    redirect(s"${routeBasePath}/${URLEncoder.encode(url, "utf-8")}")
  }
}

