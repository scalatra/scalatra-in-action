import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.book.chapter03.RecordStore

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new RecordStore, "/*")
  }
}
