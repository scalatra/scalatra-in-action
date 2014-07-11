package com.constructiveproof.crawler

import java.net.URL

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import org.scalatra.{AsyncResult, FutureSupport}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class AkkaCrawler(system: ActorSystem, grabActor: ActorRef) extends CrawlerStack with FutureSupport {

  protected implicit def executor: ExecutionContext = system.dispatcher

  implicit val defaultTimeout = new Timeout(2 seconds)

  get("/") {
    contentType = "text/html"
    new AsyncResult {
      val is = grabActor ? new URL(params("url"))
    }
  }

}
