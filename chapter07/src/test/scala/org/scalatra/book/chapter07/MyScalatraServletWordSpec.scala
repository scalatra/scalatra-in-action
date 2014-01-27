package org.scalatra.book.chapter07

import org.scalatra.test.scalatest._

class MyScalatraServletWordSpec extends ScalatraWordSpec {
  addServlet(classOf[MyScalatraServlet], "/*")

  "GET / on MyScalatraServlet" must {
    "return status 200" in {
      get("/") {
        status should equal(200)
      }
    }

    "be HTML" in {
      get("/") {
        header("Content-Type") should startWith("text/html;")
      }
    }

    "should say \"Hi, world!\"" in {
      get("/") {
        body should include("Hi")
      }
    }
  }
}
