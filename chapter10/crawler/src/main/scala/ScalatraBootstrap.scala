import javax.servlet.ServletContext

import akka.actor.ActorSystem
import com.constructiveproof.crawler._
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {


  // Get a handle to an ActorSystem and a reference to one of your actors
  val system = ActorSystem()

  override def init(context: ServletContext) {
    context.mount(new CrawlController, "/*")
  }
}
