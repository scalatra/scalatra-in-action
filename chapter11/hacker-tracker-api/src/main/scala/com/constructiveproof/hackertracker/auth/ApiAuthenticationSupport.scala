package com.constructiveproof.hackertracker.auth

import org.scalatra.{BadRequest, ScalatraBase}

trait ApiAuthenticationSupport {
  self: ScalatraBase =>

  protected def requireValidApiKey() = {
    if (!validKey(params.getOrElse("apiKey", die))) {
      die
    }
  }

  protected def validKey(apiKey: String):Boolean = {
    val validKeys = List("foo-key", "bar-key")

    if (validKeys.contains(apiKey)) {
      true
    } else {
      false
    }
  }

  protected def die = halt(BadRequest("Please provide a valid apiKey parameter"))

}
