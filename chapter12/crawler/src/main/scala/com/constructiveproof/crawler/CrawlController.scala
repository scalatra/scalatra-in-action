package com.constructiveproof.crawler

import java.net.URL
import java.nio.charset.StandardCharsets

import org.scalatra.{AsyncResult, FutureSupport}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source


class CrawlController extends CrawlerStack with FutureSupport {

  protected implicit def executor = ExecutionContext.global

  get("/") {
    contentType = "text/html"
    new AsyncResult { val is =
      Grabber.evaluate(new URL(params("url")))
    }
  }
}

object Grabber {
  def evaluate(url: URL)(implicit ctx: ExecutionContext): Future[String] = {
    Future {
      val content = Source.fromURL(
        url, StandardCharsets.UTF_8.name()
      ).mkString
      content.contains("Scala") match {
        case true => "It's a Scala site, very cool."
        case false => "Whoops, you've made some sort " +
          "of mistake in your reading choices."
      }
    }
  }
}
