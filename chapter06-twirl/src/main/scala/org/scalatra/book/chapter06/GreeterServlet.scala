package org.scalatra.book.chapter07

import org.scalatra.ScalatraServlet

class GreeterServlet extends ScalatraServlet {
  get("/greet/:whom") {
    contentType = "text/html"
    val lucky =
      for (i <- (1 to 5).toList)
      yield util.Random.nextInt(48) + 1
    html.greeting(params("whom"), lucky)
  }
}
