import sbt._
import Keys._
import play.twirl.sbt.SbtTwirl

object Chapter07TwirlBuild extends Build {
  val Organization = "org.scalatra"
  val Name = "Chapter7Twirl"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0.RC1"

  import java.net.URL
  import com.earldouglas.xsbtwebplugin.PluginKeys.port
  import com.earldouglas.xsbtwebplugin.WebPlugin.{container, webSettings}

  def Conf = config("container")

  def jettyPort = 8080

  lazy val project = Project (
    Name,
    file("."),
    settings = webSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      port in Conf := jettyPort,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.1.5.v20140505" % "container",
        "org.eclipse.jetty" % "jetty-plus" % "9.1.5.v20140505" % "container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0"
      ),
      browseTask
    )
  ).enablePlugins(SbtTwirl) 

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
