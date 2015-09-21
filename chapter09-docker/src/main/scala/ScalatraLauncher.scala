import org.eclipse.jetty.server.Server

import org.eclipse.jetty.server._
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.book.chapter09.AppConfig
import org.scalatra.servlet.ScalatraListener

object ScalatraLauncher extends App {

  val appConfig = AppConfig.load

  val server = new Server
  server.setStopTimeout(5000)
  server.setStopAtShutdown(true)

  val connector = new ServerConnector(server)
  connector.setPort(appConfig.port)

  server.addConnector(connector)

  val webAppContext = new WebAppContext
  webAppContext.setContextPath("/")
  webAppContext.setResourceBase(appConfig.webappBase)
  webAppContext.setEventListeners(Array(new ScalatraListener))
  server.setHandler(webAppContext)

  server.start
  server.join

}