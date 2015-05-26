import sbt._
import Keys._
import org.scalatra.sbt._
import com.mojolly.scalate.ScalatePlugin._

object Chapter10Build extends Build {

  val Organization = "org.scalatra"
  val Name = "Chapter 10"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0.RC1"
  val JettyVersion = "9.1.3.v20140225"

  val mySettings = Defaults.defaultConfigs ++
    ScalatraPlugin.scalatraSettings ++
    scalateSettings ++
    Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
        "org.scalaz" %% "scalaz-core" % "7.1.0",
        "ch.qos.logback" % "logback-classic" % "1.0.6", //% "runtime",
        "com.typesafe.slick" %% "slick" % "3.0.0",
        "com.h2database" % "h2" % "1.4.181",
        "com.mchange" % "c3p0" % "0.9.2",
        "org.eclipse.jetty" % "jetty-webapp" % JettyVersion % "compile;container",
        "org.eclipse.jetty" % "jetty-plus" % JettyVersion % "container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0"        
      )
    )

  lazy val project = Project("chapter10", file("."))
    .settings(mySettings:_*)

}
