package org.scalatra.book.chapter06

import org.json4s._
import org.scalatra._
import org.scalatra.json._

import scalaz._, Scalaz._

trait MyJsonScalazRoutes extends ScalatraBase with JacksonJsonSupport {

  // be able to handle scalaz' \/ as return value, simply unwrap the value from the container
  override def renderPipeline: RenderPipeline = ({
    case \/-(r) => r
    case -\/(l) => l
  }: RenderPipeline) orElse super.renderPipeline

  post("/foods_alt") {

    for {
      label <- (parsedBody \ "label").extractOpt[String] \/> BadRequest()
      fairTrade <- (parsedBody \ "fairTrade").extractOpt[Boolean] \/> BadRequest()
      tags <- (parsedBody \ "tags").extractOpt[List[String]] \/> BadRequest()
    } yield (label, fairTrade, tags)

  }

}







