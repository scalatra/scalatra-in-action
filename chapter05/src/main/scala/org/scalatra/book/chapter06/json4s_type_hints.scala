package org.scalatra.book.chapter06

import org.json4s._

import org.json4s.Extraction.decompose
import org.json4s.jackson.JsonMethods.pretty

sealed trait Measure
case class Gram(value: Double) extends Measure
case class Teaspoon(value: Double) extends Measure
case class Tablespoon(value: Double) extends Measure
case class Handful(value: Double) extends Measure
case class Pieces(value: Double) extends Measure
case class Milliliter(value: Double) extends Measure

object json4s_default_type_hint {

  val hints = ShortTypeHints(List(
    classOf[Gram],
    classOf[Tablespoon],
    classOf[Teaspoon],
    classOf[Handful]))

  implicit val formats = DefaultFormats.withHints(hints)

  val amounts = List(Handful(2), Gram(300), Teaspoon(2))
  val amountsJson = decompose(amounts)

  pretty(amountsJson)
  // [ {
  //  "jsonClass" : "Handful",
  //  "v" : 2
  // }, ...
  // ]

  amountsJson.extract[List[Measure]]
  // res: List[Amount] = List(Handful(2), Gram(300), Teaspoon(2))


}

object json4s_custom_type_hint {

  val hints = FullTypeHints(List(
    classOf[Gram],
    classOf[Tablespoon],
    classOf[Teaspoon],
    classOf[Handful]))

  implicit val formats = new DefaultFormats {
    override val typeHintFieldName: String = "_type"
  }

  val amounts = List(Handful(2), Gram(300), Teaspoon(2))
  val amountsJson = decompose(amounts)

  pretty(amountsJson)
  // [ {
  //  "jsonClass" : "Handful",
  //  "v" : 2
  // }, ...
  // ]

  amountsJson.extract[List[Measure]]
  // res: List[Amount] = List(Handful(2), Gram(300), Teaspoon(2))


}
