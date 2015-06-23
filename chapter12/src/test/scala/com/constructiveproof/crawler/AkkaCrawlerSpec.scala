package com.constructiveproof.crawler

import akka.actor.{Props, ActorSystem}
import com.constructiveproof.crawler.actors.GrabActor
import org.scalatest.FunSuite
import org.scalatra.test.scalatest.ScalatraSuite

class AkkaCrawlerSpec extends FunSuite with ScalatraSuite {

  val system = ActorSystem()
  val grabActor = system.actorOf(Props[GrabActor])

  addServlet(new AkkaCrawler(system, grabActor), "/*")

  test("Simple get") {
    get("/", Map("url" -> "http://www.google.com")) {
      status should equal(200)
    }
  }


}
