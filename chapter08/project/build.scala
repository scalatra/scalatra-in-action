import org.scalatra.sbt.ScalatraPlugin
import com.earldouglas.xwp.JettyPlugin
import sbt._
import Keys._


object Chapter08Build extends Build {
  val Organization = "org.scalatra"
  val Name = "Chapter8"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0"

  lazy val project = Project (
    Name,
    file("."),
    settings = Defaults.defaultConfigs ++ ScalatraPlugin.scalatraSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      fork in Test := true,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-json" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
        "org.json4s"   %% "json4s-jackson" % "3.3.0",
        "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
      )
    )
  ).enablePlugins(JettyPlugin)
}
