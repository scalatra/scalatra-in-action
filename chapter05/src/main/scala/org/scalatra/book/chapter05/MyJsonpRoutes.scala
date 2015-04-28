package org.scalatra.book.chapter05

import org.json4s._
import org.json4s.JsonDSL._

import org.scalatra._
import org.scalatra.json._

trait MyJsonpRoutes extends ScalatraBase with JacksonJsonSupport {

  override def jsonpCallbackParameterNames = Seq("callback")

  // implicit val jsonFormats = DefaultFormats

  get("/foods/foo_bar") {
    val productJson =
      ("label" -> "Foo bar") ~
        ("fairTrade" -> true) ~
        ("tags" -> List("bio", "chocolate"))

    productJson
  }

}





