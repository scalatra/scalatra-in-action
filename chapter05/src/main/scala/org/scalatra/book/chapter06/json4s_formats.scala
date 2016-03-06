package org.scalatra.book.chapter06

import org.json4s._

import org.json4s.Extraction.decompose
import org.json4s.jackson.Serialization.{write}
import org.json4s.jackson.{parseJson, compactJson}

import java.util.Date

object json4s_formats_default {

  implicit val formats = DefaultFormats

  val txt = compactJson(decompose(Map("date" -> new Date())))
  // txt: String = {"date":"2014-11-19T19:34:34Z"}

  val date = (parseJson(txt) \ "date").extractOpt[Date]
  // date: Option[java.util.Date] = Some(Wed Nov 19 20:34:34 CET 2014)

}

object json4s_formats_custom {

  import java.text.SimpleDateFormat

  implicit val formats = new DefaultFormats {
    override protected def dateFormatter: SimpleDateFormat = {
      new SimpleDateFormat("MM/dd/yyyy")
    }
  }
  val txt = compactJson(decompose(Map("date" -> new Date())))
  // txt: String = {"date":"11/19/2014"}

  val date = (parseJson(txt) \ "date").extractOpt[Date]
  // date: Option[java.util.Date] = Some(Wed Nov 19 00:00:00 CET 2014)

}


object json4s_formats_custom2 {

  class Foo(x: String, val y: String) {
    private val a: String = "a"
    var b: String = "b"
  }

  implicit val formats = DefaultFormats

  val foo = new Foo("x", "y")

  val txt1 = write(foo)
  // txt1: String = {"y":"y"}

}


object json4s_formats_custom3 {

  class Foo(x: String, val y: String) {
    private val a: String = "a"
    var b: String = "b"
  }

  implicit val formats = DefaultFormats + new FieldSerializer[Foo]()

  val foo = new Foo("x", "y")

  val txt1 = write(foo)
  // txt1: String = {"y":"y","a":"a","b":"b"}

}


