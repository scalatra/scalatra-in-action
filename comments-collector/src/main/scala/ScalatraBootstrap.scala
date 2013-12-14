import comments._
import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.swagger._

// TODO
// optimize imports

class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new Swagger("1.0", "1")

  override def init(context: ServletContext) {
    context.mount(new CommentsServlet(), "/*")
    context.mount(new ResourcesApp(), "/api-docs/*")
  }
}
