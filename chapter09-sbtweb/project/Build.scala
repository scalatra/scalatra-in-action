import sbt._
import Keys._

import com.earldouglas.xwp.JettyPlugin
import com.earldouglas.xwp.WebappPlugin.autoImport._
import com.earldouglas.xwp.JettyPlugin.autoImport._
import com.earldouglas.xwp.ContainerPlugin.start
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
  val Name = "Chapter9SBTweb"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0.RC1"

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
        "com.typesafe" % "config" % "1.2.1",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
      ),
      sourceDirectory in webappPrepare := (resourceDirectory in Assets).value,
      target in webappPrepare := (stagingDirectory in Assets).value,
      (test in Test) <<= (test in Test) dependsOn (stage in Assets),
      (start in Jetty) <<= (start in Jetty) dependsOn (stage in Assets)
    )

  val myScalateSettings =
    ScalatePlugin.scalateSettings ++ Seq(
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile) { base =>
        Seq(
          TemplateConfig(
            base / "public" / "WEB-INF" / "templates",      // use src/main/public
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
    includeFilter in filter := "*.less" || "*.css.map",
    pipelineStages := Seq(filter)
  )

  // a light-weight development SBT environment, does not run the full asset pipeline
  val webDevelopmentSettings = Seq(
    pipelineStages := Seq()
  )

  // choose between settings: dev or env
  val env = sys.props.getOrElse("env", "dev")

  val webSettings = {
    if (env == "dev") webDevelopmentSettings
    else webProductionSettings
  }

  lazy val project = Project("chapter09-sbtweb", file("."))
    .enablePlugins(SbtWeb, JettyPlugin)
    .settings(mySettings: _*)
    .settings(myScalateSettings: _*)
    .settings(webSettings: _*)

}
