package com.constructiveproof.crawler

import java.net.URL

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem}
import org.scalatra.AsyncResult

/**
 * Created by dave on 11/07/2014.
 */
class AkkaCrawler(system: ActorSystem, grabActor: ActorRef) extends CrawlerStack {

  

  get("/") {
    contentType = "text/html"
    new AsyncResult {
      val is = grabActor ? new URL(params("url"))
    }
  }

}
