import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.book.chapter05.DocumentStorage

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new DocumentStorage, "/*")
  }
}
