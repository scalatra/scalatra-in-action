package org.scalatra.book.chapter13

import org.scalatra._
import scalate.ScalateSupport

class Chapter13 extends Chapter13Stack {

  get("/") {
    "Hello, from action!"
  }
  
}
