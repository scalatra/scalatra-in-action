import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.book.chapter08._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new MyScalatraServlet, "/*")
    context.mount(new NukeLauncherServlet(RealNukeLauncher), "/nuke/*")
  }
}
