import org.eclipse.jetty.server.{Server, ServerConnector}
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.book.chapter09.AppConfig
import org.scalatra.servlet.ScalatraListener

object ScalatraLauncher extends App {

  val conf = AppConfig.load

  val server = new Server
  server.setStopTimeout(5000)
  server.setStopAtShutdown(true)

  val connector = new ServerConnector(server)
  connector.setHost("127.0.0.1")
  connector.setPort(conf.port)

  server.addConnector(connector)

  val webAppContext = new WebAppContext
  webAppContext.setContextPath("/")
  webAppContext.setResourceBase(conf.assetsDirectory)
  webAppContext.setEventListeners(Array(new ScalatraListener))
  server.setHandler(webAppContext)

  server.start
  server.join

}