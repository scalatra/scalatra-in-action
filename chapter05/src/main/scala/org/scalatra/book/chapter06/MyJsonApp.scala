package org.scalatra.book.chapter06

import org.json4s._
import org.scalatra._

class MyJsonApp extends ScalatraServlet with MyJsonRoutes with MyJsonScalazRoutes with MyFoodRoutes with MyJsonpRoutes {

  implicit lazy val jsonFormats = DefaultFormats +
    new NutritionFactsSerializer

}
