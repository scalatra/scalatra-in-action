import org.eclipse.jetty.server._
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

object ScalatraLauncher extends App {

  // load config
  // -DresourceBase=target/webapp
  val resourceBase = sys.props.getOrElse("resourceBase", "src/main/webapp")
  val host = "localhost"
  val port = 8080

  // create jetty server
  val server = new Server
  server.setStopTimeout(5000)
  server.setStopAtShutdown(true)

  // create http connector
  val connector = new ServerConnector(server)
  connector.setHost(host)
  connector.setPort(port)

  server.addConnector(connector)

  // create web application context and set context listener
  val webAppContext = new WebAppContext
  webAppContext.setContextPath("/")
  webAppContext.setResourceBase(resourceBase)
  webAppContext.setEventListeners(Array(new ScalatraListener))

  // set handler and start server
  server.setHandler(webAppContext)
  server.start
  server.join

}
