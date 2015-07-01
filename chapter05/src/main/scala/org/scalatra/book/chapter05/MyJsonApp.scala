package org.scalatra.book.chapter05

import org.json4s._
import org.scalatra._

class MyJsonApp extends ScalatraServlet with MyJsonRoutes with MyFoodRoutes {

  implicit lazy val jsonFormats = DefaultFormats +
    new NutritionFactsSerializer

}
