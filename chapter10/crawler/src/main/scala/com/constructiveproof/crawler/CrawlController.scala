package com.constructiveproof.crawler

import java.net.URL
import java.nio.charset.StandardCharsets

import scala.io.Source

class CrawlController extends CrawlerStack {

  get("/") {
    contentType = "text/html"
    Grabber.evaluate(new URL(params("url")))
  }
}

object Grabber {

  def evaluate(url: URL): String = {
    val content = Source.fromURL(
      url, StandardCharsets.UTF_8.name()
    ).mkString
    content.contains("Scala") match {
      case true => "It's a Scala site, very cool."
      case false => "Whoops, you've made some sort of mistake in your reading choices."
    }
  }

}