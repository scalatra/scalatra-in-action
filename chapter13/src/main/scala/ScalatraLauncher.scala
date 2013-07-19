import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

class ScalatraLauncher extends App {

  // TODO determine values
  val resourceBase = "webapp"
  val port = 8080
  val host = "localhost"

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
  context.setResourceBase(resourceBase)

  // adds the ServletContextListener, supports setting theLifeCycle via web.xml
  if (Option(context.getInitParameter(ScalatraListener.LifeCycleKey)).isEmpty) {
    context.addEventListener(new ScalatraListener)
  }

  // default servlet: context.addServlet(classOf[DefaultServlet], "/")

  server.setHandler(context)

  server.start
  server.join

}
