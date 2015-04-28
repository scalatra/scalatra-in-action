package org.scalatra.book.chapter09

import org.scalatra.test.specs2._

class Chapter09Spec extends ScalatraSpec { def is =
  "Chapter09"                     ^
    "/ should should execute an action"         ! action ^
    "/static.txt should return static file"     ! staticFile ^
                                                end

  val conf = AppConfig.load

  addServlet(new Chapter09(conf), "/*")

  def action = get("/") {
    status must_== 200
  }

  def staticFile = get("/static.txt") {
    status must_== 200
    body must_== "this is static text!"
  }

}
