import sbt._
import Keys._

import com.earldouglas.xwp.XwpPlugin._
import com.slidingautonomy.sbt.filter.Import._
import com.typesafe.sbt.web.Import.WebKeys._
import com.typesafe.sbt.web.Import._
import com.typesafe.sbt.web.SbtWeb

import org.scalatra.sbt._

import com.mojolly.scalate._
import com.mojolly.scalate.ScalatePlugin._
import com.mojolly.scalate.ScalatePlugin.ScalateKeys._

object Chapter09SbtWebBuild extends Build {
  val Organization = "org.scalatra"
  val Name = "Chapter 9 - SBT web"
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

  val webProductionSettings = Seq(
    // webappSrc in webapp := (resourceDirectory in Assets).value,
    // webappDest in webapp := stagingDirectory.value,   // in webapp scope is not respected by dist (!)
    webappSrc := (resourceDirectory in Assets).value,
    webappDest := stagingDirectory.value, // check
    includeFilter in filter := "*.less" || "*.css.map",
    pipelineStages := Seq(filter)
  )

  // a development SBT environment, which does not run the full asset pipeline
  val webDevelopmentSettings = Seq(
    webappSrc := (resourceDirectory in Assets).value,
    webappDest := stagingDirectory.value, // check
    pipelineStages := Seq()
  )

  val env = sys.props.getOrElse("env", "production")
  val webSettings = {
    if (env == "dev") webDevelopmentSettings
    else webProductionSettings
  }

  lazy val project = Project("chapter09-sbtweb", file("."))
    .enablePlugins(SbtWeb)
    .settings(mySettings: _*)
    .settings(myScalateSettings: _*)
    .settings(webSettings: _*)

}
