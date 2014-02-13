package com.constructiveproof.hackertracker.stacks

import org.scalatra.json.JacksonJsonSupport
import org.scalatra.swagger.{SwaggerSupport, JacksonSwaggerBase}
import com.constructiveproof.hackertracker.auth.ApiAuthenticationSupport
import org.json4s.DefaultFormats

trait ApiStack extends HackerCoreStack with ApiAuthenticationSupport
  with JacksonJsonSupport with JacksonSwaggerBase {

  override protected implicit val jsonFormats = DefaultFormats

}
