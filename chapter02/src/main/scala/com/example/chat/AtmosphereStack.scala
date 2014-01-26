package com.example.chat

import org.scalatra._

// Imports for Atmosphere communication
import atmosphere._
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s._

trait AtmosphereStack extends ScalatraServlet with AtmosphereSupport 
  with JValueResult with JacksonJsonSupport with SessionSupport {

  implicit protected val jsonFormats: Formats = DefaultFormats

}