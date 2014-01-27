package org.scalatra.book.chapter07

import org.scalatra.ScalatraServlet

class MyScalatraServlet extends ScalatraServlet {
  get("/") {
    <html>
      <body>
        <h1>Hi, world!</h1>
      </body>
    </html>
  }
}
