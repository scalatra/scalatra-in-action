import org.scalatra.book.chapter10.{DbSetup, Chapter10App}

import org.scalatra._
import javax.servlet.ServletContext

import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits._

class ScalatraBootstrap extends LifeCycle {

  val jdbcUrl = "jdbc:h2:mem:chapter10;DB_CLOSE_DELAY=-1"
  val jdbcDriverClass = "org.h2.Driver"
  val db = Database.forURL(jdbcUrl, driver = jdbcDriverClass)

  override def init(context: ServletContext): Unit = {
    db.run(DbSetup.createDatabase) foreach { x =>
      context.mount(Chapter10App(db), "/*")
    }
  }

  override def destroy(context: ServletContext): Unit = {
    super.destroy(context)

    db.close()
  }

}
