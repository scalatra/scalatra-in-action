
import com.typesafe.config.Config

case class AppConfig(config: Config) {
  val port = config.getInt("app.port")
  val hostname = config.getString("app.hostname")
  val useHttps = config.getBoolean("app.useHttps")
  val forceHttps = config.getBoolean("app.forceHttps")
  val lifecycle = if (config.hasPath("app.lifecycle")) Some(config.getString("app.lifecycle")) else None
}