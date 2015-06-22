package org.scalatra.book.chapter08

import org.json4s.JString
import org.scalatra.test.scalatest._

class FoodServletWordSpec extends ScalatraWordSpec 
  with JsonBodySupport {
  
  addServlet(classOf[FoodServlet], "/*")

  "GET /foods/potatoes on FoodServlet" must {
    "return status 200" in {
      get("/foods/potatoes") {
        status should equal(200)
      }
    }

    "be JSON" in {
      get("/foods/potatoes") {
        header("Content-Type") should startWith("application/json;")
      }
    }

    "should have name potatoes" in {
      get("/foods/potatoes") {
        jsonBody \ "name" should equal(JString("potatoes"))
      }
    }
  }
}

