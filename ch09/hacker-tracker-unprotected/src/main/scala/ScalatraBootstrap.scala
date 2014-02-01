import com.constructiveproof.hackertracker._
import com.constructiveproof.hackertracker.init.DatabaseInit
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle with DatabaseInit {

  override def init(context: ServletContext) {
    configureDb()
    context.mount(new HackersController, "/hackers")
    context.mount(new DatabaseSetupController, "/database")
  }

  override def destroy(context:ServletContext) {
    closeDbConnection()
  }
}
