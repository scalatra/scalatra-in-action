import javax.servlet.ServletContext

import _root_.akka.actor.{Props, ActorSystem}
import com.constructiveproof.crawler._
import com.constructiveproof.crawler.actors.GrabActor
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {

  val system = ActorSystem()
  val grabActor = system.actorOf(Props[GrabActor])

  override def init(context: ServletContext) {
    context.mount(new CrawlController, "/*")
    context.mount(new AkkaCrawler(system, grabActor), "/akka/*")
  }
}
