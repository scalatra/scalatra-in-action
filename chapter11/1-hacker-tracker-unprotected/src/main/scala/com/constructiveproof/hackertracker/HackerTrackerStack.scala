package com.constructiveproof.hackertracker

import org.scalatra._
import scalate.ScalateSupport
import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import javax.servlet.http.HttpServletRequest
import collection.mutable
import com.constructiveproof.hackertracker.init.DatabaseSessionSupport

trait HackerTrackerStack extends ScalatraServlet with ScalateSupport
  with DatabaseSessionSupport with FlashMapSupport with MethodOverride {

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
