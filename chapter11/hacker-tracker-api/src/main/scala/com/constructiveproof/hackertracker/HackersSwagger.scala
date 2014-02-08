package com.constructiveproof.hackertracker

import org.scalatra.swagger.{Swagger, JacksonSwaggerBase}
import org.scalatra.ScalatraServlet

class HackersSwagger(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase {

}
