package org.scalatra.book.chapter05

import org.scalatra._
import org.scalatra.json._

trait MyJsonpRoutes extends ScalatraBase with JacksonJsonSupport {

  override def jsonpCallbackParameterNames = Seq("jsonp")

}
