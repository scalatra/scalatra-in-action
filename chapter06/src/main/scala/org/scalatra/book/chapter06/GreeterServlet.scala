package org.scalatra.book.chapter07

import org.scalatra.ScalatraServlet

class GreeterServlet extends MyScalatraWebappStack {
  get("/greet/:whom") {
    contentType = "text/html"
    val lucky =
      for (i <- (1 to 5).toList)
      yield util.Random.nextInt(48) + 1
    layoutTemplate("greeter_dry.html",
      "whom" -> params("whom"),
      "lucky" -> lucky)
  }
}
