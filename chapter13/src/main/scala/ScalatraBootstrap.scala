import org.scalatra.book.chapter13._
import org.scalatra._
import org.scalatra.ScalatraBase.{PortKey, HostNameKey, ForceHttpsKey}

import com.typesafe.config.ConfigFactory
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  def environment(implicit context: ServletContext): String = {
    sys.props.get(EnvironmentKey) orElse Option(context.getInitParameter(EnvironmentKey)) getOrElse ("DEVELOPMENT")
  }

  override def init(implicit context: ServletContext) {

    val config = AppConfig(ConfigFactory.load(environment))

    context.initParameters(HostNameKey) = config.hostname
    context.initParameters(PortKey) = config.port.toString
    context.initParameters(ForceHttpsKey) = config.forceHttps.toString

    context.mount(new Chapter13, "/*")
  }

}
