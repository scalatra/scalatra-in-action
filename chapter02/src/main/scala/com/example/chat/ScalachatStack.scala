package com.example.chat

import org.scalatra._
import scalate.ScalateSupport
import org.fusesource.scalate.{ TemplateEngine, Binding }
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import javax.servlet.http.HttpServletRequest
import collection.mutable

trait ScalachatStack extends ScalatraServlet with ScalateSupport
  with AtmosphereStack {
  
  /* wire up the precompiled templates */
  override protected def defaultTemplatePath: 
    List[String] = List("/templates/views")

  override protected def createTemplateEngine(config: ConfigT) = {
    val engine = super.createTemplateEngine(config)
    engine.layoutStrategy = new DefaultLayoutStrategy(engine,
      TemplateEngine.templateTypes.map(
        "/templates/layouts/default." + _): _*)

    engine.packagePrefix = "templates"
    engine
  }
  /* end wiring up the precompiled templates */
  
  override protected def templateAttributes(implicit request:
    HttpServletRequest): mutable.Map[String, Any] = {
      // Add extra attributes here, they need bindings in the build file
      super.templateAttributes ++ mutable.Map.empty 
  }
  

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
