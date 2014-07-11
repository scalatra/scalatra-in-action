package com.constructiveproof.crawler

import java.net.URL

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import org.scalatra.AsyncResult
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext

/**
 * Created by dave on 11/07/2014.
 */
class AkkaCrawler(system: ActorSystem, grabActor: ActorRef) extends CrawlerStack {


  protected implicit def executor: ExecutionContext = system.dispatcher

  implicit val defaultTimeout = new Timeout(2 seconds)

  get("/") {
    contentType = "text/html"
    new AsyncResult {
      val is = grabActor ? new URL(params("url"))
    }
  }

}
