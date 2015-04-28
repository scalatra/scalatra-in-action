package org.scalatra.book.chapter05

import org.scalatra._
import org.scalatra.json._

import org.json4s._
import org.json4s.JsonDSL._

trait MyJsonRoutes extends ScalatraBase with JacksonJsonSupport {

  // implicit val jsonFormats = DefaultFormats

  get("/foods/foo_bar") {
    val productJson =
      ("label" -> "Foo bar") ~
        ("fairTrade" -> true) ~
        ("tags" -> List("bio", "chocolate"))

    productJson
  }

  post("/foods") {

    def parseProduct(jv: JValue): (String, Boolean, List[String]) = {
      val label = (jv \ "label").extract[String]
      val fairTrade = (jv \ "fairTrade").extract[Boolean]
      val tags = (jv \ "tags").extract[List[String]]

      (label, fairTrade, tags)
    }

    val product = parseProduct(parsedBody)
    println(product)
  }

}







