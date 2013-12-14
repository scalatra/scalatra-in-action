package comments

import org.scalatra._
import org.scalatra.scalate.ScalateSupport

class CommentsFrontend(comments: CommentsRepository) extends ScalatraServlet with ScalateSupport {

  get("/") {
    "frontend"
  }

  notFound {
    // anything here?
  }

}

