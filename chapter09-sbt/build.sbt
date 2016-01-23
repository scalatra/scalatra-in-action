
organization := "org.scalatra"
name := "Chapter 9"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.11.6"

fork in Test := true

val ScalatraVersion = "2.4.0"

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "com.typesafe" % "config" % "1.2.1",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
)

enablePlugins(JettyPlugin)

containerPort in Jetty := 8090
