package org.scalatra.book.chapter10

import slick.driver.H2Driver.api._
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatra.test.scalatest.ScalatraSuite

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class AppSpec extends FunSuite with ScalatraSuite with BeforeAndAfter {

  val jdbcUrl = "jdbc:h2:mem:chapter10;DB_CLOSE_DELAY=-1"
  val jdbcDriverClass = "org.h2.Driver"
  val db = Database.forURL(jdbcUrl, driver = jdbcDriverClass)
  val res = db.run(DbSetup.createDatabase)

  Await.result(res, Duration(2, "seconds"))

  addServlet(new Chapter10App(db), "/*")

  test("get all areas") {
    get("/areas") {
      status should equal(200)
    }
  }

}
