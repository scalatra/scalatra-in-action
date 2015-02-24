package org.scalatra.book.chapter13

class Chapter13 extends Chapter13Stack {

  get("/?") {
    f"Hello, from action! (isDevelopment = ${isDevelopmentMode}, serverHost = ${serverHost}, forceSsl = ${needsHttps}})"
  }
  
}
