package org.scalatra.book.chapter08

import org.scalatra.test.specs2._
import org.specs2.mutable.After

class NukeLauncherSpec extends MutableScalatraSpec with After {
  sequential

  val stubLauncher = new StubNukeLauncher
  addServlet(new NukeLauncherServlet(stubLauncher), "/*")

  def after: Any = stubLauncher.isLaunched = false

  def launch[A](code: String)(f: => A): A = post("/launch", "code" -> code) { f }

  "The wrong pass code" should {
    "respond with forbidden" in {
      launch("wrong") {
        status must_== 403
      }
    }

    "not launch the nukes" in {
      launch("wrong") {
        stubLauncher.isLaunched must_== false
      }
    }
  }

  "The right pass code" should {
    "launch the nukes" in {
      launch("password123") {
        stubLauncher.isLaunched must_== true
      }
    }
  }
}
