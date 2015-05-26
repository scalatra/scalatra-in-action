import sbt._
import Keys._


object Chapter08Build extends Build {
  val Organization = "org.scalatra"
  val Name = "Chapter8"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0.RC1"
  val JettyVersion = "9.1.3.v20140225"

  import java.net.URL
  import com.earldouglas.xsbtwebplugin.PluginKeys.port
  import com.earldouglas.xsbtwebplugin.WebPlugin.{container, webSettings}

  def Conf = config("container")

  def jettyPort = 8080

  lazy val project = Project (
    Name,
    file("."),
    settings = Defaults.defaultSettings ++ webSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      port in Conf := jettyPort,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion,
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",        
        "org.eclipse.jetty" % "jetty-webapp" % JettyVersion % "container",
        "org.eclipse.jetty" % "jetty-plus" % JettyVersion % "container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0"
      ),
      browseTask
    )
  )

  val browse = TaskKey[Unit]("browse", "open web browser to localhost on container:port")
  val browseTask = browse <<= (streams, port in container.Configuration) map { (streams, port) =>
    import streams.log
    val url = new URL("http://localhost:%s" format port)
    try {
      log info "Launching browser."
      java.awt.Desktop.getDesktop.browse(url.toURI)
    }
    catch {
      case t: Throwable => {
        log info { "Could not open browser, sorry. Open manually to %s." format url.toExternalForm }
      }
    }
  }
}
