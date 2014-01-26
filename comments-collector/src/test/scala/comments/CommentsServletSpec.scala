package comments

import org.scalatra.test.specs2._

import org.scalatra.swagger._
import com.mongodb.casbah.Imports._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class CommentsServletSpec extends ScalatraSpec { def is =
  "GET / on CommentsServlet"                     ^
    "should return status 200"                  ! root200^
                                                end

  implicit val apiInfo = new ApiInfo("The comments API",
    "Docs for the comments API",
    "http://www.manning.com/carrero2/",
    "Ross", "MIT", "http://scalatra.org")

  implicit val swagger = new Swagger("1.0", "1", apiInfo)

  val mongoClient = MongoClient()
  val mongoColl = mongoClient("comments_collector")("comments")
  val comments = CommentsRepository(mongoColl)

  addServlet(new CommentsApi(comments), "/api/*")

  def root200 = get("/api/comments") {
    status must_== 200
  }
}
