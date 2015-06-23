import _root_.akka.actor.{Props, ActorSystem}
import com.constructiveproof.crawler._
import com.constructiveproof.crawler.actors.GrabActor
import org.apache.spark.SparkContext
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  val sc = new SparkContext("local[8]", "Spark Demo")

  override def init(context: ServletContext) {
    val system = ActorSystem()
    val grabActor = system.actorOf(Props[GrabActor])

    context.mount(new CrawlController, "/*")
    context.mount(new AkkaCrawler(system, grabActor), "/akka/*")
    context.mount(new SparkExampleController(sc), "/spark/*")
  }

  override def destroy(context: ServletContext) {
    sc.stop()
  }
}
