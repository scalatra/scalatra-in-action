package org.scalatra.book.chapter06

import org.json4s._
import org.json4s.JsonDSL._

object json4s_basics {

  val fooBar = JObject(
    "label" -> JString("Foo bar"),
    "fairTrade" -> JBool(true),
    "tags" -> JArray(List(JString("bio"), JString("chocolate"))))

  // fooBar: org.json4s.JValue = JObject(List((label,JString(Foo bar)), ...

  import org.json4s.jackson.parseJson

  val txt =
    """{
      | "tags": ["bio","chocolate"],
      | "label": "Foo bar",
      | "fairTrade": true
      |}""".stripMargin

  val parsed = parseJson(txt)
  // parsed: org.json4s.JValue = JObject(List((label,JString(Foo bar)), ...

  fooBar == parsed
  // res11: Boolean = true

}
