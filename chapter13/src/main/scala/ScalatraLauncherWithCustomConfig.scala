
import com.typesafe.config.{ConfigFactory, Config}
import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.ScalatraBase
import org.scalatra.servlet.ScalatraListener

import ScalatraBase.{PortKey, HostNameKey, ForceHttpsKey}
import ScalatraListener.LifeCycleKey
import org.scalatra.EnvironmentKey

case class AppConfig(config: Config) {
  val port = config.getInt("app.port")
  val hostname = config.getString("app.hostname")
  val useHttps = config.getBoolean("app.useHttps")
  val forceHttps = config.getBoolean("app.forceHttps")
  val environment = config.getString("app.environment")
  val lifecycle = if (config.hasPath("app.lifecycle")) Some(config.getString("app.lifecycle")) else None
}

object ScalatraLauncherWithCustomConfig extends App {

  val config = AppConfig(ConfigFactory.load())

  // start server
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

  // set init parameters
  Map(EnvironmentKey -> config.environment,
    PortKey -> config.port,
    HostNameKey -> config.hostname,
    ForceHttpsKey -> config.forceHttps,
    LifeCycleKey -> config.lifecycle).foreach {
    case (key, None) =>
    case (key, value) => context.setInitParameter(key, value.toString)
  }

  context.setEventListeners(Array(new ScalatraListener))

  // default servlet: context.addServlet(classOf[DefaultServlet], "/")
  server.setHandler(context)

  server.start
  server.join

}
