package org.scalatra.book.chapter08

import org.scalatra.test.specs2._

class MyScalatraServletSpec extends ScalatraSpec { def is =
  "GET / on MyScalatraServlet"                     ^
    "should return status 200"                     ! root200^
    "should be HTML"                               ! rootHtml^
    "should say \"Hi, world!\""                    ! rootHi^
    end

  addServlet(classOf[MyScalatraServlet], "/*")

  def root200 = get("/") {
    status must_== 200
  }

  def rootHtml = get("/") {
    header("Content-Type") must startWith("text/html;")
  }

  def rootHi = get("/") {
    body must contain("Hi, world!")
  }
}
