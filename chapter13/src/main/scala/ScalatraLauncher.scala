
import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.ScalatraBase
import org.scalatra.servlet.ScalatraListener

import ScalatraBase.{PortKey, HostNameKey}

object ScalatraLauncher extends App {

  //  val config = ConfigFactory.load()
  //  val port = config.getInt(PortKey)
  //  val host = config.getString(HostNameKey)
  val port = sys.props(PortKey).toInt
  val host = sys.props(HostNameKey)

  // start server
  val server = new Server

  server.setGracefulShutdown(5000)
  server.setSendServerVersion(false)
  server.setSendDateHeader(true)
  server.setStopAtShutdown(true)

  val connector = new SelectChannelConnector
  connector.setHost(host)
  connector.setPort(port)
  connector.setMaxIdleTime(90000)
  server.addConnector(connector)

  val context = new WebAppContext
  context.setContextPath("/")
  context.setResourceBase("webapp")
  context.setEventListeners(Array(new ScalatraListener))

  // default servlet: context.addServlet(classOf[DefaultServlet], "/")
  server.setHandler(context)

  server.start
  server.join

}
