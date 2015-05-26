package org.scalatra.book.chapter07

import org.scalatest.FunSuite
import org.scalatra.test.scalatest.ScalatraSuite

class GreeterServletSpec extends FunSuite with ScalatraSuite {
  addServlet(classOf[GreeterServlet], "/*")

  test("simple get") {
    get("/greet/dave") {
      status should equal(200)
    }
  }
}
