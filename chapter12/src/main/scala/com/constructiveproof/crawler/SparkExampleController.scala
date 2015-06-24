package com.constructiveproof.crawler

import org.apache.spark.SparkContext
import org.scalatra.FutureSupport
import scala.concurrent.{Future, ExecutionContext}

class SparkExampleController(sc: SparkContext) extends CrawlerStack with FutureSupport {

  protected implicit def executor: ExecutionContext = ExecutionContext.global

  get("/count/:word") {
    val word = params("word")
    Future {
      val occurrenceCount = WordCounter.count(word, sc)
      s"Found $occurrenceCount occurrences of $word"
    }
  }

}

object WordCounter  {

  def count(word: String, sc: SparkContext) = {
    val data = "/Users/dave/Desktop/data.csv" // <-- Change this to match your file location and name
    val lines = sc.textFile(data)
    lines.filter(line => line.contains(word)).cache().count()
  }

}