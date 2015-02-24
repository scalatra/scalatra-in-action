import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

import ScalatraListener.LifeCycleKey

object ScalatraLauncher extends App {

  val server = new Server
  server.setGracefulShutdown(5000)
  server.setSendServerVersion(false)
  server.setSendDateHeader(true)
  server.setStopAtShutdown(true)

  val connector = new SelectChannelConnector
  connector.setHost("localhost")
  connector.setPort(8080)
  connector.setMaxIdleTime(90000)
  server.addConnector(connector)

  val context = new WebAppContext
  context.setContextPath("/")
  context.setResourceBase("src/main/webapp")

  context.setEventListeners(Array(new ScalatraListener))

  server.setHandler(context)

  server.start
  server.join

}
