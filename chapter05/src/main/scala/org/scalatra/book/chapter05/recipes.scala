package org.scalatra.book.chapter05

case class Recipe(title: String, details: RecipeDetails,
  ingredients: List[IngredientLine], steps: List[String])

case class RecipeDetails(cuisine: String, vegetarian: Boolean,
  diet: Option[String])

case class IngredientLine(label: String, quantity: String)

trait RecipeRepository {

  def create(title: String,
    details: RecipeDetails,
    ingredients: List[IngredientLine],
    steps: List[String]): Recipe

  def byTitle(title: String): Option[Recipe]

}

class MemoryRecipeRepository  extends RecipeRepository {

  private val store = collection.mutable.Map[String, Recipe]()

  def create(title: String, details: RecipeDetails,
    ingredients: List[IngredientLine],
    steps: List[String]): Recipe = {
    val recipe = Recipe(title, details, ingredients, steps)
    store(title) = recipe
    recipe
  }

  def byTitle(title: String): Option[Recipe] = store.get(title)

}