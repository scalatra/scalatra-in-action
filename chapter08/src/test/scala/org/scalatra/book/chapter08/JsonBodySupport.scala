package org.scalatra.book.chapter08

import org.scalatra.test._
import org.scalatra.test.specs2._

import org.json4s.JValue
import org.json4s.jackson.JsonMethods

trait JsonBodySupport { self: ScalatraTests =>
  def jsonBody: JValue = JsonMethods.parse(body)
}
