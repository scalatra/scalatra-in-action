package org.scalatra.book.chapter08

import org.scalatra.test.specs2._

import org.json4s._
import org.json4s.jackson.JsonMethods

class FoodServletSpec extends ScalatraSpec with JsonBodySupport {
  def is = s2"""
GET /foods/potatoes on FoodServlet
  should return status 200                      $potatoesOk
  should be JSON                                $potatoesJson
  should contain name potatoes                  $potatoesName
  """

  addServlet(classOf[FoodServlet], "/*")

  def potatoesOk = get("/foods/potatoes") {
    status must_== 200
  }

  def potatoesJson = get("/foods/potatoes") {
    header("Content-Type") must startWith ("application/json;")
  }

  def potatoesName = get("/foods/potatoes") {
    jsonBody \ "name" must_== JString("potatoes")
  }
}
