import com.constructiveproof.hackertracker._
import com.constructiveproof.hackertracker.init.DatabaseInit
import javax.servlet.ServletContext
import org.scalatra.LifeCycle
import org.scalatra.swagger.{ApiInfo, Swagger}

class ScalatraBootstrap extends LifeCycle with DatabaseInit {

  implicit val apiInfo = new ApiInfo(
  "The HackerTracker API",
  "Docs for the HackerTracker API",
  "http://www.constructiveproof.com/hacker-tracker/tos.html",
  "apiteam@constructiveproof.com",
  "MIT",
  "http://opensource.org/licenses/MIT")

  implicit val swagger = new Swagger("1.2", "1.0.0", apiInfo)

  override def init(context: ServletContext) {
    configureDb()
    context.mount(new ApiController, "/api/hackers/*")
    context.mount(new HackersSwagger, "/api-docs/*")
    context.mount(new DatabaseSetupController, "/database")
    context.mount(new HackersController, "/hackers")
    context.mount(new SessionsController, "/sessions")
  }

  override def destroy(context:ServletContext) {
    closeDbConnection()
  }
}
