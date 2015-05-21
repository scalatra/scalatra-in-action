import sbt._
import Keys._

import org.scalatra.sbt._
import org.scalatra.sbt.DistPlugin._
import org.scalatra.sbt.DistPlugin.DistKeys._

import com.mojolly.scalate._
import com.mojolly.scalate.ScalatePlugin._
import com.mojolly.scalate.ScalatePlugin.ScalateKeys._

object Chapter09StandaloneBuild extends Build {
  val Organization = "org.scalatra"
  val Name = "Chapter 9 - Standalone"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.4"
  val ScalatraVersion = "2.4.0.M2"

  val mySettings =
    ScalatraPlugin.scalatraSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "com.typesafe" % "config" % "1.0.2",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.3.v20140905",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
      )
    )

  val myScalateSettings =
    ScalatePlugin.scalateSettings ++ Seq(
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile) { base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty, /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ), /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )

  val myDistSettings =
    DistPlugin.distSettings ++ Seq(
      mainClass in Dist := Some("ScalatraLauncher"),
      memSetting in Dist := "2g",
      permGenSetting in Dist := "256m",
      envExports in Dist := Seq("LC_CTYPE=en_US.UTF-8", "LC_ALL=en_US.utf-8"),
      javaOptions in Dist ++= Seq("-Xss4m",
        "-Dfile.encoding=UTF-8",
        "-Dlogback.configurationFile=logback.production.xml",
        "-Dorg.scalatra.environment=production")
    )

  lazy val project = Project("chapter09-standalone", file("."))
    .settings(mySettings: _*)
    .settings(myScalateSettings: _*)
    .settings(myDistSettings: _*)

}
