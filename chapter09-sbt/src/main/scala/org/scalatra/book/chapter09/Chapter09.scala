package org.scalatra.book.chapter09

import org.scalatra.ScalatraServlet

class Chapter09(appConfig: AppConfig) extends ScalatraServlet {

  get("/") {
    f"Greetings! (isDevelopment = ${isDevelopmentMode}})"
  }

  get("/shorten-url") {
    val token = UrlShortener.nextFreeToken
    f"${appConfig.webBase}/$token"
  }

}
