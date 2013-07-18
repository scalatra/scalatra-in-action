import sbt._
import Keys._

import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._

import org.scalatra.sbt.DistPlugin._
import org.scalatra.sbt.DistPlugin.DistKeys

import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

import sbtassembly.Plugin._
import AssemblyKeys._

object Chapter13Build extends Build {
  val Organization = "org.scalatra"
  val Name = "chapter13"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.2"
  val ScalatraVersion = "2.2.1"

  val myDependencies = Seq(
      "org.scalatra" %% "scalatra" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
      "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
      "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
      "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container",
      "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
    )

  val mySettings = Defaults.defaultSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= myDependencies,
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      },
      mainClass in Dist := Some("foo")
    ) ++ DistPlugin.webDistSettings

  // settings for sbt-assembly plugin
  // val assemblySettings = Seq(

  //     // handle conflicts during assembly task
  //     mergeStrategy in assembly <<= (mergeStrategy in assembly) {
  //       (old) => {
  //         case "about.html" => MergeStrategy.first
  //         case x => old(x)
  //       }
  //     },

  //     // copy web resources to /webapp folder
  //     resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map {
  //       (managedBase, base) =>
  //         val webappBase = base / "src" / "main" / "webapp"
  //         for {
  //           (from, to) <- webappBase ** "*" x rebase(webappBase, managedBase / "main" / "webapp")
  //         } yield {
  //           Sync.copy(from, to)
  //           to
  //         }
  //     }
  //   )

  lazy val project = Project(
    "chapter13",
    file("."),
    settings = mySettings ++ ScalatraPlugin.scalatraWithJRebel ++ scalateSettings
  )

}
