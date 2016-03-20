package org.scalatra.book.chapter06

import org.scalatra.test.specs2._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class FilesSpec extends ScalatraSpec { def is =
  "GET / on DocumentsApp"                     ^
    "should return status 200"                  ! root200^
    end

  val store = DocumentStore("data")

  val app = new DocumentsApp(store)

  addServlet(app, "/*")

  def root200 = get("/") {
    status must_== 200
  }
}
