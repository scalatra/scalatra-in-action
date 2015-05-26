package com.constructiveproof.crawler

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatra.test.scalatest.ScalatraSuite


// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class CrawlControllerSpec extends FunSuite with ScalatraSuite with BeforeAndAfter {

//  addServlet(classOf[CrawlController], "/*")

  test("simple get") {
    true should equal(true)
//    get("/") {
//      status should equal(200)
//    }
  }
}
