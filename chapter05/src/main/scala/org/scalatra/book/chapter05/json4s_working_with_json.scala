package org.scalatra.book.chapter05

import org.json4s._
import org.json4s.JsonDSL._

object json4s_working_with_json extends App {

  // producing JSON

  val jsString: JValue = "italian"
  // jsString: org.json4s.JValue = JString(italian)

  val jsBool: JValue = true
  // jsBool: org.json4s.JValue = JBool(true)


  val detailsJson =
    ("cuisine" ->  "italian") ~ ("vegetarian" ->  true)
  // detailsJson: org.json4s.JsonAST.JObject =
  //   JObject(List((cuisine,JString(italian)), (vegetarian,JBool(true))))

  val tags: JValue = List("higher", "cuisine")

  val recipeJson =
    ("title"   ->  "Penne with cocktail tomatoes, Rucola and Goat cheese") ~
      ("details" -> detailsJson) ~
      ("ingredients" -> List(
        ("label" ->  "Penne")             ~ ( "quantity" ->  "250g"),
        ("label" ->  "Cocktail tomatoes") ~ ( "quantity" ->  "300g"),
        ("label" ->  "Rucola")            ~ ( "quantity" ->  "2 handful"),
        ("label" ->  "Goat cheese")       ~ ( "quantity" ->  "250g"),
        ("label" ->  "Garlic cloves")     ~ ( "quantity" ->  "250g"))) ~
      ("steps" -> List(
        "Cook noodles until aldente.",
        "Quarter the tomatoes, wash the rucola, dice the goat's cheese ...",
        "Heat olive oil in a pan, add the garlic and the tomatoes and ...",
        "Shortly before the noodles are ready add the rucola to the ...",
        "Drain the noodles and mix with the tomatoes, finally add the ..."))


  val jsObject1: JValue = ("details" -> RecipeDetails("italian", true, None))
  // <console>:23: error: No implicit view available from RecipeDetails => org.json4s.JsonAST.JValue.
  //         val jsObject: JValue = ("details" -> RecipeDetails("italian", true, None))

  implicit lazy val formats = DefaultFormats

  implicit def details2jvalue(rd: RecipeDetails): JValue =
    Extraction.decompose(rd)

  val jsObject2: JValue = ("details" -> RecipeDetails("italian", true, None))
  // jsObject2: org.json4s.JValue = JObject(List((details,JObject(List((cuisine,JString(italian)), (vegetarian,JBool(true)))))))


  // consuming JSON

  assert(recipeJson \ "title" == JString("Penne with cocktail tomatoes, Rucola and Goat cheese"))
  // res: JString(Penne with cocktail tomatoes, Rucola and Goat cheese)


  assert(recipeJson \ "details" \ "cuisine" == JString("italian"))
  // res: JString(italian)


  recipeJson \ "ingredients" \ "label"
  // res: JArray(List(JString(Penne), JString(Cocktail tomatoes), ...))

  assert(recipeJson \\ "cuisine" == JString("italian"))
  // res: JString(italian)

  assert(recipeJson \ "details" \ "prerequisites" == JNothing)
  // res: JNothing


  // extracting values from JSON

  assert((recipeJson \ "title").extract[String] == "Penne with cocktail tomatoes, Rucola and Goat cheese")
  // res: String = Penne with cocktail tomatoes, Rucola and Goat cheese

  assert((recipeJson \ "steps").extract[List[String]].size == 5)
  // res: List[String] = List(Cook noodles until aldente., ...)

  assert((recipeJson \ "details").extract[RecipeDetails] == RecipeDetails("italian", true, None))
  // res: RecipeDetails = RecipeDetails(italian,true,None)


  // extracting optional values from JSON

  assert(JString("foo").extractOpt[String] == Some("foo"))
  // res: Option[String] = Some(foo)

  assert(JString("foo").extractOpt[Boolean] == None)
  // res: Option[Boolean] = None

  assert(JNull.extractOpt[String] == None)
  // res: Option[String] = None

  assert(JNothing.extractOpt[String] == None)
  // res: Option[String] = None

}
