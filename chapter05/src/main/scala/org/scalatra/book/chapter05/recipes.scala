package org.scalatra.book.chapter05

case class Recipe(title: String, details: RecipeDetails,
  ingredients: List[IngredientLine], steps: List[String])

case class RecipeDetails(cuisine: String, vegetarian: Boolean,
  diet: Option[String])

case class IngredientLine(label: String, quantity: String)

