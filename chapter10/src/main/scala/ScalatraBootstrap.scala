import com.mchange.v2.c3p0.ComboPooledDataSource
import org.scalatra.book.chapter10.{DbSetup, Chapter10App, ClimbingRoutesRepository}
import org.slf4j.LoggerFactory

import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  val logger = LoggerFactory.getLogger(getClass)

  logger.info("Creating c3p0 connection pool")
  val cpds = new ComboPooledDataSource

  override def init(context: ServletContext) {

    // Slick driver
    val slickDriver = slick.driver.H2Driver

    // Build database, repository & application
    val db = slick.jdbc.JdbcBackend.Database.forDataSource(cpds)
    val repo = new ClimbingRoutesRepository(slickDriver)
    val app = Chapter10App(db, repo)

    db withTransaction { implicit session =>
      DbSetup.createDatabase
    }

    context.mount(app, "/*")
  }

  override def destroy(context: ServletContext) {
    super.destroy(context)
    logger.info("Closing c3po connection pool")
    cpds.close
  }

}




