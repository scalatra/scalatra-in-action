package org.scalatra.book.chapter09

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport

class Chapter09(appConfig: AppConfig) extends ScalatraServlet with ScalateSupport {

  get("/?") {
    f"Hello, from action! (isDevelopment = ${isDevelopmentMode}})"
  }

  get("/shorten-url") {
    val token = UrlShortener.nextFreeToken
    f"${appConfig.webBase}/$token"
  }

  notFound {
    // remove content type in case it was set through an action
    contentType = null
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }

}
