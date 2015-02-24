package org.scalatra.book.chapter13

import org.scalatra.test.specs2._

class Chapter13Spec extends ScalatraSpec { def is =
  "Chapter13"                     ^
    "/ should should execute an action"         ! action ^
    "/static.txt should return static file"     ! staticFile ^
    "/scalate should render a template"         ! scalate ^
                                                end

  addServlet(classOf[Chapter13], "/*")

  def action = get("/") {
    status must_== 200
  }

  def staticFile = get("/static.txt") {
    status must_== 200
    body must_== "this is static text!"
  }

  def scalate = get("/scalate") {
    status must_== 200
    body must_== """<p>Hello, Scalate!</p>
"""
  }

}
