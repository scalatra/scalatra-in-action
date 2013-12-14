import comments._
import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.swagger._
import com.mongodb.casbah.Imports._

class ScalatraBootstrap extends LifeCycle {

  // create an implicit instance of ApiInfo which publishes additional information
  implicit val apiInfo = new ApiInfo("The comments API",
    "Docs for the comments API",
    "http://www.manning.com/carrero2/",
    "Ross", "MIT", "http://scalatra.org")

  // create an implicit instance of Swagger which is passed to both servlets
  implicit val swagger = new Swagger("1.0", "1", apiInfo)

  // create a mongodb client and collection
  val mongoClient = MongoClient()
  val mongoColl = mongoClient("comments_collector")("comments")

  override def init(context: ServletContext) {

    // create a comments repository using the mongo collection
    val comments = CommentsRepository(mongoColl)

    // mount the api + swagger docs
    context.mount(new CommentsApi(comments), "/api/*")
    context.mount(new CommentsApiDoc(), "/api-docs/*")

    // mount the html frontend
    context.mount(new CommentsFrontend(comments), "/*")

  }

  override def destroy(context: ServletContext) {

    // shutdown the mongo client
    mongoClient.close

  }
}
