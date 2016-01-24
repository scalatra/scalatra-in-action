import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.book.chapter06.{DocumentStore, DocumentsApp}

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext) {

    val store = DocumentStore("data")

    val app = new DocumentsApp(store)

    context.mount(app, "/*")

  }
}
