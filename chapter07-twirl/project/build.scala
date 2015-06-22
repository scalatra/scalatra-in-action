import sbt._
import Keys._

import org.scalatra.sbt.ScalatraPlugin

import play.twirl.sbt.SbtTwirl

object Chapter07TwirlBuild extends Build {
  val Organization = "org.scalatra"
  val Name = "Chapter7Twirl"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0.RC1"

  lazy val project = Project (
    Name,
    file("."),
    settings = Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
      )
    )
  ).settings(ScalatraPlugin.scalatraSettings:_*)
   .enablePlugins(SbtTwirl)
}
