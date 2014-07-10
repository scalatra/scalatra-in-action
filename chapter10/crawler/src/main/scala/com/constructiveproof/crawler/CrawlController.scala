package com.constructiveproof.crawler

import java.net.URL
import java.nio.charset.StandardCharsets

import scala.concurrent.ExecutionContext
import scala.io.Source

class CrawlController extends CrawlerStack {

  protected implicit def executor: ExecutionContext = ExecutionContext.global

  get("/") {
    contentType = "text/html"
    Grabber.grab(new URL("https://constructiveproof.com"))
  }
}

object Grabber {

  def grab(url: URL)(implicit ctx: ExecutionContext): String = {
    Source.fromURL(
      new URL("https://constructiveproof.com"), StandardCharsets.UTF_8.name()
    ).mkString
  }

}