package com.constructiveproof.hackertracker.stacks

import org.scalatra._
import org.scalatra.scalate.ScalateSupport

trait BrowserStack extends HackerCoreStack with ScalateSupport
  with FlashMapSupport {

  notFound {
    // remove content type in case it was set through an action
    contentType = null
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }
}
