package org.scalatra.book.chapter05

import org.json4s._
import org.json4s.JsonDSL._

import org.scalatra._
import org.scalatra.json._

trait MyJsonpRoutes extends ScalatraBase with JacksonJsonSupport {

  override def jsonpCallbackParameterNames = Seq("callback")

}





