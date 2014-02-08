import com.constructiveproof.hackertracker._
import com.constructiveproof.hackertracker.init.DatabaseInit
import javax.servlet.ServletContext
import org.scalatra._
import org.scalatra.swagger.{ApiInfo, Swagger}

class ScalatraBootstrap extends LifeCycle with DatabaseInit {

  override def init(context: ServletContext) {
    implicit val apiInfo = new ApiInfo(
      "The HackerTracker API",
      "Docs for the HackerTracker API",
      "http://www.constructiveproof.com/hacker-tracker/tos.html",
      "Dave Hrycyszyn",
      "MIT",
      "http://constructiveproof.com/hacker-tracker/api/license.html")

    implicit val swagger = new Swagger("1.0", "1", apiInfo)

    configureDb()
    context.mount(new ApiController, "/api/hackers")
    context.mount(new DatabaseSetupController, "/database")
    context.mount(new HackersController, "/hackers")
    context.mount(new HackersSwagger, "/api-docs/*")
    context.mount(new SessionsController, "/sessions")
  }

  override def destroy(context:ServletContext) {
    closeDbConnection()
  }
}
