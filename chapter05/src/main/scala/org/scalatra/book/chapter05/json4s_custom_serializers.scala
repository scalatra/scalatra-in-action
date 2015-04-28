package org.scalatra.book.chapter05

import org.json4s._
import org.json4s.JsonDSL._

import org.json4s.Extraction.decompose
import org.json4s.jackson.JsonMethods.{parse, pretty}

sealed trait Fact
case class Energy(value: Int) extends Fact
case class Carbohydrate(value: Double) extends Fact
case class Fat(value: Double) extends Fact
case class Protein(value: Double) extends Fact

case class NutritionFacts(
  energy: Energy,
  carbohydrate: Carbohydrate,
  fat: Fat,
  protein: Protein)

class NutritionFactsSerializer
  extends CustomSerializer[NutritionFacts](implicit formats => ({
    case jv: JValue =>
      val e = (jv \ "energy").extract[Int]
      val c = (jv \ "carbohydrate").extract[Double]
      val f = (jv \ "fat").extract[Double]
      val p = (jv \ "protein").extract[Double]

      NutritionFacts(
        Energy(e), Carbohydrate(c), Fat(f), Protein(p))
  }, {
    case facts: NutritionFacts =>
      ("energy" -> facts.energy.value) ~
        ("carbohydrate" -> facts.carbohydrate.value) ~
        ("fat" -> facts.fat.value) ~
        ("protein" -> facts.protein.value)
  }))

