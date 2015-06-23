package org.scalatra.book.chapter08

import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.scalatra.ScalatraServlet
import org.scalatra.json._

class FoodServlet extends ScalatraServlet with JacksonJsonSupport {

  implicit lazy val jsonFormats = DefaultFormats

  get("/foods/potatoes") {
    val productJson =
      ("name" -> "potatoes") ~
        ("fairTrade" -> true) ~
        ("tags" -> List("vegetable", "tuber"))

    productJson
  }
}
