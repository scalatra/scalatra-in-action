package org.scalatra.book.chapter06

import org.scalatra._
import org.scalatra.json._

trait MyJsonpRoutes extends ScalatraBase with JacksonJsonSupport {

  override def jsonpCallbackParameterNames = Seq("jsonp")

}
