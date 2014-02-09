package com.constructiveproof.hackertracker.stacks

import org.scalatra.json.JacksonJsonSupport
import org.scalatra.swagger.{SwaggerSupport, JacksonSwaggerBase}

trait ApiStack extends HackerCoreStack
  with JacksonJsonSupport with JacksonSwaggerBase {

}
