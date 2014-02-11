package com.constructiveproof.hackertracker.stacks

import org.scalatra.json.JacksonJsonSupport
import org.scalatra.swagger.{SwaggerSupport, JacksonSwaggerBase}
import org.json4s.DefaultFormats

trait ApiStack extends HackerCoreStack
  with JacksonJsonSupport with JacksonSwaggerBase {

  override protected implicit val jsonFormats = DefaultFormats

}
