package com.constructiveproof.hackertracker.auth

import org.scalatra.{Unauthorized, ScalatraBase}

trait ApiAuthenticationSupport {
  self: ScalatraBase =>

  protected def requireValidApiKey() = {
    if (!validKey(params.getOrElse("apiKey", unauthorized))) {
      unauthorized
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

  protected def unauthorized = halt(Unauthorized("Please provide a valid apiKey parameter"))

}
