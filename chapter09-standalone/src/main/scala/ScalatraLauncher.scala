import org.eclipse.jetty.server.Server

import org.eclipse.jetty.server._
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

object ScalatraLauncher extends App {

  // in development mode start with -DresourceBase=target/webapp
  val resourceBase = sys.props.getOrElse("resourceBase", "webapp")
  val host = "localhost"
  val port = 8083

  val server = new Server
  server.setStopTimeout(5000)
  server.setStopAtShutdown(true)

  val connector = new ServerConnector(server)
  connector.setHost(host)
  connector.setPort(port)

  server.addConnector(connector)

  val webAppContext = new WebAppContext
  webAppContext.setContextPath("/")
  webAppContext.setResourceBase(resourceBase)
  webAppContext.setEventListeners(Array(new ScalatraListener))
  server.setHandler(webAppContext)

  server.start
  server.join

}