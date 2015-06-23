package org.scalatra.book.chapter03

import org.scalatest.FunSuite
import org.scalatra.test.scalatest.ScalatraSuite

class RecordStoreSpec  extends FunSuite with ScalatraSuite  {

  addServlet(new RecordStore("/"), "/*")

  test("Getting artists") {
    get("/artists/?") {
      status should equal(200)
    }
  }

}
