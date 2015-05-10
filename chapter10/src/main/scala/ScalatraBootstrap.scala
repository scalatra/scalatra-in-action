import javax.servlet.ServletContext

import org.scalatra._
import org.scalatra.book.chapter10.{Chapter10App, DbSetup}
import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ScalatraBootstrap extends LifeCycle {

  val jdbcUrl = "jdbc:h2:mem:chapter10;DB_CLOSE_DELAY=-1"
  val jdbcDriverClass = "org.h2.Driver"
  val db = Database.forURL(jdbcUrl, driver = jdbcDriverClass)

  val app = new Chapter10App(db)

  override def init(context: ServletContext): Unit = {

    val res = db.run(DbSetup.createDatabase)

    Await.result(res, Duration(5, "seconds"))

    context.mount(app, "/*")

  }

  override def destroy(context: ServletContext): Unit = {
    db.close()
  }

}
