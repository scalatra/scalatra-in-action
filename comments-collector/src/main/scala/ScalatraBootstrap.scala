import comments._
import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.swagger._
import com.mongodb.casbah.Imports._

// TODO
// optimize imports
class ScalatraBootstrap extends LifeCycle {

  // create an implicit instance of Swagger which is passed to both servlets
  implicit val swagger = new Swagger("1.0", "1")

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
