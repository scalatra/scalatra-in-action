package org.scalatra.book.chapter09

class Chapter09 extends Chapter09Stack {

  get("/?") {
    f"Hello, from action! (isDevelopment = ${isDevelopmentMode}, serverHost = ${serverHost}, forceSsl = ${needsHttps}})"
  }

}
