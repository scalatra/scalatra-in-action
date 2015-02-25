import org.scalatra.book.chapter09._
import org.scalatra._
import org.scalatra.ScalatraBase.{PortKey, HostNameKey, ForceHttpsKey}

import com.typesafe.config.ConfigFactory
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext) {

    val conf = AppConfig.load
    sys.props(org.scalatra.EnvironmentKey) = AppEnvironment.asString(conf.env)

    val app = new Chapter09(conf)
    context.mount(app, "/*")

  }

}
