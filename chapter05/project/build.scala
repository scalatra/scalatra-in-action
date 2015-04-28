import org.scalatra.sbt.ScalatraPlugin
import sbt._
import Keys._

object Chapter05Build extends Build {

  val Organization = "org.scalatra"
  val Name = "chapter05"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.5"
  val ScalatraVersion = "2.4.0.M2"

  val mySettings = Defaults.defaultConfigs ++
    ScalatraPlugin.scalatraWithDist ++
    Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "org.scalatra" %% "scalatra-json" % ScalatraVersion,
        "org.json4s"   %% "json4s-jackson" % "3.2.10",
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
      )
    )

  lazy val project = Project(Name, file("."))
    .settings(mySettings:_*)

}
