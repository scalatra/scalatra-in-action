import org.scalatra.book.chapter09._
import org.scalatra._
import org.scalatra.ScalatraBase.{PortKey, HostNameKey, ForceHttpsKey}

import com.typesafe.config.ConfigFactory
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  def environment(context: ServletContext): String = {
    sys.props.get(EnvironmentKey) orElse Option(context.getInitParameter(EnvironmentKey)) getOrElse ("development")
  }

  override def init(context: ServletContext) {

    val config = AppConfig(ConfigFactory.load(environment(context)))

    context.initParameters(HostNameKey) = config.hostname
    context.initParameters(PortKey) = config.port.toString
    context.initParameters(ForceHttpsKey) = config.forceHttps.toString

    context.mount(new Chapter09, "/*")
  }

}
