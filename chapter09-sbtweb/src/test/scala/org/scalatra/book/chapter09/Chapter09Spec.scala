package org.scalatra.book.chapter09

import org.eclipse.jetty.servlet.ServletContextHandler
import org.scalatra.test.specs2._

class Chapter09Spec extends MutableScalatraSpec {

  val conf = AppConfig.load
  sys.props(org.scalatra.EnvironmentKey) = AppEnvironment.asString(conf.env)

  override lazy val servletContextHandler = {
    val handler = new ServletContextHandler(ServletContextHandler.SESSIONS)
    handler.setContextPath(contextPath)
    handler.setResourceBase("target/web/stage")
    handler
  }

  addServlet(new Chapter09(conf), "/*")

  "/ should should execute an action" in {
    get("/") {
      status must_== 200
    }
  }

  "/static.txt should return static file" in {
    get("/static.txt") {
      status must_== 200
      body must_== "this is static text!"
    }
  }

  "/hello-scalate should render a template" in {
    get("/hello-scalate") {
      status must_== 200
      body must_==
        """<p>Hello, Scalate!</p>
          |""".stripMargin
    }
  }

  "/css/main.css should return a CSS file" in {
    get("/css/main.css") {
      status must_== 200
    }
  }

}
