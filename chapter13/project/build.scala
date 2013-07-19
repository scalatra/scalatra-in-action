import sbt._
import Keys._
import sbt._
import collection.JavaConverters._

import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._

import org.scalatra.sbt.DistPlugin._
import org.scalatra.sbt.DistPlugin.DistKeys._

import com.mojolly.scalate._
import com.mojolly.scalate.ScalatePlugin._
import com.mojolly.scalate.ScalatePlugin.ScalateKeys._

object Chapter13Build extends Build {
  val Organization = "org.scalatra"
  val Name = "chapter13"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.2"
  val ScalatraVersion = "2.2.1"

  val myProjectSettings = Seq(
    organization := Organization,
    name := Name,
    version := Version,
    scalaVersion := ScalaVersion,
    resolvers += Classpaths.typesafeReleases,
    libraryDependencies ++= Seq(
      "org.scalatra" %% "scalatra" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
      "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
      "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
      "com.typesafe" % "config" % "1.0.2",
      "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "compile;container",
      "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
    )
  )

  val myScalatraSettings = ScalatraPlugin.scalatraSettings

  val myScalateSettings = ScalatePlugin.scalateSettings ++ Seq(
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

  val myDistSettings = DistPlugin.distSettings ++ Seq(
    mainClass in Dist := Some("ScalatraLauncher"),
    memSetting in Dist := "1g",
    permMemSetting in Dist := "256m",
    envExports in Dist := Seq("LC_CTYPE=en_US.UTF-8", "LC_ALL=en_US.utf-8"),
    javaOptions in Dist <++= (memSetting in Dist, permMemSetting in Dist) map { (mem, perm) =>
      val rr = Seq(
        "-Xms" + mem,
        "-Xmx" + mem,
        "-XX:PermSize="+perm,
        "-XX:MaxPermSize="+perm,
        "-Xss4m",
        "-Dfile.encoding=UTF-8",
        "-DloggerPath=conf/log4j.properties"
      )
      rr
    },
    templatesConfig in Dist <<= (templatesConfig in Dist) { templates =>
      templates
    },
    templatesData in Dist <<= (mainClass in Dist, name in Dist, javaOptions in Dist, envExports in Dist,
      version) map { (mn, nm, jo, ee, ver) =>
      if (mn.isEmpty) sys.error("You need to specify a main class")
      Map(
        "mainClass" -> mn.get,
        "name" -> nm,
        "javaOptions" -> jo.asJava,
        "envExports" -> ee.asJava,
        "version" -> ver
      )
    }
  )

  val mySettings = myProjectSettings ++ myScalatraSettings ++ myScalateSettings ++ myDistSettings

  lazy val project = Project("chapter13", file(".")).settings(mySettings :_*)

}
