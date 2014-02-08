package com.constructiveproof.hackertracker.stacks

import org.scalatra.json.JacksonJsonSupport
import org.scalatra.swagger.JacksonSwaggerBase

trait ApiStack extends HackerCoreStack
  with JacksonJsonSupport with JacksonSwaggerBase{

}
