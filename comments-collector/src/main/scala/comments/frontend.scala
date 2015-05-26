package comments

import org.scalatra._
import org.scalatra.scalate.ScalateSupport
import java.net.URLEncoder

import scalaz._, Scalaz._

class CommentsFrontend(commentsRepo: CommentsRepository) extends ScalatraServlet with ScalateSupport with ScalazSupport {
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
    for {
      url <- params("url").right
      title <- params.get("title") \/> BadRequest("title is required")
      body <- params.get("body") \/> BadRequest("body is required")
    } yield {
      commentsRepo.create(url, title, body)
      Found(s"${routeBasePath}/${URLEncoder.encode(url, "utf-8")}")
    }
  }
}

