import sbt._
import Keys._
import play.twirl.sbt.SbtTwirl

object Chapter06Build extends Build {
  val Organization = "org.scalatra"
  val Name = "chapter06-twirl"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.3"
  val ScalatraVersion = "2.3.0.M1"

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
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion,
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
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
