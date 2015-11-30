import sbt._
import Keys._

import com.earldouglas.xwp.JettyPlugin
import com.earldouglas.xwp.WebappPlugin.autoImport._
import com.mojolly.scalate._
import com.mojolly.scalate.ScalatePlugin._
import com.mojolly.scalate.ScalatePlugin.ScalateKeys._
import sbtdocker.DockerKeys._
import sbtdocker.{DockerPlugin, ImageName}
import sbtdocker.mutable.Dockerfile

object Chapter09Docker extends Build {

  val Organization = "org.scalatra"
  val Name = "chapter09-docker"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0.RC1"
  val DockerImageName = ImageName("org.scalatra/chapter09-docker")

  val mySettings =
    Seq(
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
        "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.10.v20150310",
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

  val myDockerSettings = Seq(

    mainClass := Some("ScalatraLauncher"),

    exportJars := true,
    docker <<= docker.dependsOn(sbt.Keys.`package`),

    imageNames in docker := Seq(DockerImageName),

    dockerfile in docker := {

      val classpath = (fullClasspath in Runtime).value
      val webappDir = (target in webappPrepare).value

      val mainclass = mainClass.value.getOrElse(sys.error("Expected exactly one main class"))

      // Make a colon separated classpath with the JAR files
      val classpathString = classpath.files.map("/app/lib/" + _.getName).mkString(":")

      new Dockerfile {

        from("ubuntu:14.04")

        // Install packages
        runRaw("apt-get update")
        runRaw("apt-get install -y vim curl wget unzip")

        // Install Oracle Java JDK 1.8.x
        runRaw("mkdir -p /usr/lib/jvm")
        runRaw(
          "wget --header \"Cookie: oraclelicense=accept-securebackup-cookie\"" +
            " -O /usr/lib/jvm/jdk-8u51.tar.gz http://download.oracle.com/" +
            "otn-pub/java/jdk/8u51-b16/jdk-8u51-linux-x64.tar.gz")
        runRaw("tar xzf /usr/lib/jvm/jdk-8u51.tar.gz --directory /usr/lib/jvm")
        runRaw("update-alternatives --install /usr/bin/java java " +
            "/usr/lib/jvm/jdk1.8.0_51/bin/java 100")
        runRaw("update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk1.8.0_51/bin/javac 100")

        // Add all .jar files
        add(classpath.files, "/app/lib/")

        // All all files from webapp
        add(webappDir, "/app/webapp")

        // Remove lib (alternatively filter out before already)
        runRaw("rm -rf /app/webapp/WEB-INF/lib")

        // Define some volumes for persistent data (containers are immutable)
        volume("/app/conf")
        volume("/app/data")

        expose(80)

        workDir("/app")

        cmdRaw(
          f"java " +
            f"-Xmx4g " +
            f"-Dlogback.configurationFile=/app/conf/logback.xml " +
            f"-Dconfig.file=/app/conf/application.conf " +
            f"-cp $classpathString $mainclass")

      }
    }

  )

  lazy val project = Project("chapter09-docker", file("."))
    .enablePlugins(JettyPlugin, DockerPlugin)
    .settings(mySettings: _*)
    .settings(myScalateSettings: _*)
    .settings(myDockerSettings: _*)

}
