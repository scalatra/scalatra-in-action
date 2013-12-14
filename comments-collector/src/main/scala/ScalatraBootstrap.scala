import comments._
import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.swagger._
import com.mongodb.casbah.Imports._

// TODO
// optimize imports
class ScalatraBootstrap extends LifeCycle {


  implicit val apiInfo = new ApiInfo("The comments API", "Docs for the comments API", "http://www.manning.com/carrero2/", "Ross", "MIT", "http://scalatra.org")

  // create an implicit instance of Swagger which is passed to both servlets
  implicit val swagger = new Swagger("1.0", "1", apiInfo)

  // create a mongodb client and collection
  val mongoClient = MongoClient()
  val mongoColl = mongoClient("comments_collector")("comments")

  override def init(context: ServletContext) {
    context.mount(new CommentsApi(mongoColl), "/api/*")
    context.mount(new CommentsApiDoc(), "/api-docs/*")
    context.mount(new CommentsFrontend(mongoColl), "/*")
  }

  override def destroy(context: ServletContext) {
    mongoClient.close
  }
}
