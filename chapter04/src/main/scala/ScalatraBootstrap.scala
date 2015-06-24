import com.constructiveproof.hackertracker._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new CookiesExample, "/cookies/*")
    context.mount(new HackersController, "/*")
  }
}
