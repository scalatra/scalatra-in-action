package com.constructiveproof.hackertracker

import org.scalatra.swagger.{Swagger, JacksonSwaggerBase}
import org.scalatra.ScalatraServlet
import org.json4s.DefaultFormats

class HackersSwagger(implicit val swagger: Swagger)
  extends ScalatraServlet with JacksonSwaggerBase {
}
