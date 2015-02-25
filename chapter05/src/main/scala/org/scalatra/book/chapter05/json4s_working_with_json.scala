package org.scalatra.book.chapter05

import org.json4s._
import org.json4s.JsonDSL._

object json4s_working_with_json {

  // producing JSON

  val jsString: JValue = "italian"
  // jsString: org.json4s.JValue = JString(italian)

  val jsBool: JValue = true
  // jsBool: org.json4s.JValue = JBool(true)


  val detailsJson =
    ("cuisine" ->  "italian") ~ ("vegetarian" ->  true) ~
      ("diet" ->  null)
  // detailsJson: org.json4s.JsonAST.JObject =
  //   JObject(List((cuisine,JString(italian)), (vegetarian,JBool(true)), (diet,null)))

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

  implicit val formats = DefaultFormats

  implicit def details2jvalue(rd: RecipeDetails): JValue =
    Extraction.decompose(rd)

  val jsObject2: JValue = ("details" -> RecipeDetails("italian", true, None))
  // jsObject2: org.json4s.JValue = JObject(List((details,JObject(List((cuisine,JString(italian)), (vegetarian,JBool(true)))))))

  val recipeRepository: RecipeRepository = new MemoryRecipeRepository
  val recipeJson2 = recipeRepository.byTitle("Foo bars") map Extraction.decompose
  // res


  // consuming JSON

  recipeJson \ "title"
  // res: JString(Penne with cocktail tomatoes, Rucola and Goat cheese)


  recipeJson \ "details" \ "cuisine"
  // res: JString(italian)


  recipeJson \ "ingredients" \ "label"
  // res: JArray(List(JString(Penne), JString(Cocktail tomatoes), ...))

  recipeJson \\ "label"
  // res: JArray(List(JString(250g), JString(300g), ...))

  recipeJson \\ "cuisine"
  // res: Jarray(List(JString(italian)))

  recipeJson \ "details" \ "prerequisites"
  // res: JNothing
  // there are no prerequisites, returns JNothing

  recipeJson \ "details" \ "diet"
  // JNull


  // extracting values from JSON

  (recipeJson \ "title").extract[String]
  // res: String = Penne with cocktail tomatoes, Rucola and Goat cheese

  (recipeJson \ "steps").extract[List[String]]
  // res: List[String] = List(Cook noodles until aldente., ...)

  (recipeJson \ "details").extract[RecipeDetails]
  // res: RecipeDetails = RecipeDetails(italian,true,None)


  // extracting optional values from JSON

  JString("foo").extractOpt[String]
  // res: Option[String] = Some(foo)

  JString("foo").extractOpt[Boolean]
  // res: Option[Boolean] = None

  JNull.extractOpt[String]
  // res: Option[String] = None

  JNothing.extractOpt[String]
  // res: Option[String] = None

}
