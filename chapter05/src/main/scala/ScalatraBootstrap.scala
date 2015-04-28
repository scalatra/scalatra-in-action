import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.book.chapter05.MyJsonApp

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new MyJsonApp, "/*")
  }
}
