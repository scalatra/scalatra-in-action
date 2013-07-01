package org.scalatra.book.chapter05.json

/**
 * This is the code for the JSON chapter introduction.
 * It teaches simple usage of the JSON data interchange format with the Json4s library.
 */
object intro {

  import org.json4s._
  import org.json4s.jackson.JsonMethods._

  val todo = JObject(
    "text" -> JString("Create a web application."),
    "date" -> JString("2013-05-29 22:02"),
    "importance" -> JInt(3),
    "tags" -> JArray(List(JString("project"), JString("useful"), JString("fun"))),
    "done" -> JBool(false))


  val todoStr = compact(todo)

  println(todoStr)
  // {"text":"Create a web startup.","date":"2013-05-29 22:02","importance":3,"tags":["project","useful","fun"],"done":false}

  println(pretty(todo))
  // {
  //   "text" : "Create a web startup.",
  //   "date" : "2013-05-29 22:02",
  //   "importance" : 3,
  //   "tags" : [ "project", "useful", "fun" ],
  //   "done" : false
  // }

  val todoParsed = parse(todoStr)
  // todoParsed: org.json4s.JValue = JObject(...

}
