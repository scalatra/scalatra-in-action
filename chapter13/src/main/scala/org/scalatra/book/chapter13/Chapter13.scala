package org.scalatra.book.chapter13

import org.scalatra._
import scalate.ScalateSupport

class Chapter13 extends Chapter13Stack {

  get("/?") {
    val key = _root_.Env.config.getString("key")
    f"Hello, from action! ($key)"
  }
  
}
