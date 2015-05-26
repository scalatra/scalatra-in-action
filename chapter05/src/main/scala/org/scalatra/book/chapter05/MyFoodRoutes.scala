package org.scalatra.book.chapter05

import org.scalatra._
import org.scalatra.json._

import org.json4s._

trait MyFoodRoutes extends ScalatraBase with JacksonJsonSupport {

  get("/foods/foo_bar/facts") {
    val facts = NutritionFacts(
      Energy(2050), Carbohydrate(36.2), Fat(33.9), Protein(7.9))

    val factsJson = Extraction.decompose(facts)

    factsJson
  }

  post("/foods/:name/facts") {
    val facts = parsedBody.extractOpt[NutritionFacts]
    println(f"updated facts: $facts")
  }

 }





