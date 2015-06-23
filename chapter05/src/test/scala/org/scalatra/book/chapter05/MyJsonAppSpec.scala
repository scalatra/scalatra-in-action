package org.scalatra.book.chapter05

import org.scalatest.FunSuite
import org.scalatra.test.scalatest.ScalatraSuite

class MyJsonAppSpec extends FunSuite with ScalatraSuite {

  addServlet(classOf[MyJsonApp], "/*")

  test("Getting foods") {
    get("/foods/foo_bar") {
      status should equal(200)
    }
  }

}
