package com.constructiveproof.hackertracker

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{JacksonSwaggerBase, Swagger}

class HackersSwagger(implicit val swagger: Swagger)
  extends ScalatraServlet with JacksonSwaggerBase