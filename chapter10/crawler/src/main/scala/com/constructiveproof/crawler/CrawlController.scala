package com.constructiveproof.crawler

import java.net.URL
import java.nio.charset.StandardCharsets

import _root_.akka.actor.ActorSystem

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

import org.scalatra._

class CrawlController(system: ActorSystem) extends CrawlerStack with FutureSupport {

  protected implicit def executor: ExecutionContext = system.dispatcher

  get("/") {
    contentType = "text/html"
    new AsyncResult {
      val is = Grabber.grab(new URL("https://constructiveproof.com"))
    }
  }
}

object Grabber {

  def grab(url: URL)(implicit ctx: ExecutionContext): Future[String] = {
    Future {
      Source.fromURL(
        new URL("https://constructiveproof.com"), StandardCharsets.UTF_8.name()
      ).mkString
    }
  }

}