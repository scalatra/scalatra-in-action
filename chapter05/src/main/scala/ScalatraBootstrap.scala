import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.book.chapter05.files.DocumentsApp
import org.scalatra.book.chapter05.json.LibraryApp

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new DocumentsApp, "/documents/*")
    context.mount(new LibraryApp, "/library/*")
  }
}
