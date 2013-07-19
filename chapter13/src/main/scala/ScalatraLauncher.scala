import com.typesafe.config.{ConfigFactory, Config}
import java.util.Locale
import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

object Env {
  val Production = "PRODUCTION"
  val Development = "DEVELOPMENT"
  val Staging = "STAGING"
  val Test = "TEST"

  private[this] def envKey = sys.env.get("SCALATRA_MODE")
  private[this] def propsKey = sys.props.get("scalatra.mode")

  val environment = (envKey orElse propsKey) getOrElse {
    println("No environment found, defaulting to: development")
    "development"
  }

  val isProduction = isEnvironment(Production) || isEnvironment(Staging)
  val isDevelopment = isEnvironment(Development)
  val isStaging = isEnvironment(Staging)
  val isTest = isEnvironment(Test)
  private def isEnvironment(env: String) = environment.toUpperCase(Locale.ENGLISH) startsWith env.toUpperCase(Locale.ENGLISH)

  val config = {
    val defaultLocalConfig = ConfigFactory.load()
    if (defaultLocalConfig.hasPath(Env.environment))
      defaultLocalConfig.getConfig(Env.environment).withFallback(defaultLocalConfig)
    else defaultLocalConfig
  }
}

object ScalatraLauncher extends App {

  import Env.config

  val resourceBase = config.getString("basedir")
  val port = config.getInt("port")
  val host = config.getString("host")

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
