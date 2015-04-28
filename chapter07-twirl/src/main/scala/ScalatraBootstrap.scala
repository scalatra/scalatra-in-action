import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.book.chapter07._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new GreeterServlet, "/*")
  }
}
