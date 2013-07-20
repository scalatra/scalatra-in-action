
import com.typesafe.config.ConfigFactory
import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

import ScalatraListener.LifeCycleKey

object ScalatraLauncherWithCustomConfig extends App {

  val config = AppConfig(ConfigFactory.load())
  val server = new Server

  server.setGracefulShutdown(5000)
  server.setSendServerVersion(false)
  server.setSendDateHeader(true)
  server.setStopAtShutdown(true)

  val connector = new SelectChannelConnector
  connector.setHost(config.hostname)
  connector.setPort(config.port)
  connector.setMaxIdleTime(90000)
  server.addConnector(connector)

  val context = new WebAppContext
  context.setContextPath("/")
  context.setResourceBase("webapp")

  config.lifecycle.foreach(l => context.setInitParameter(LifeCycleKey, l))
  context.setEventListeners(Array(new ScalatraListener))

  // default servlet: context.addServlet(classOf[DefaultServlet], "/")
  server.setHandler(context)

  server.start
  server.join

}
