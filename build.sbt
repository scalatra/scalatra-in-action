
// super project which aggregates the chapter projects
// e.g. can be used to compile and test all chapters together
// each chapter project is still a standalone SBT project

name := "scalatra-in-action"

version := "1.0"

scalaVersion := "2.11.6"

lazy val root = project.in(file("."))
  .aggregate(
    chapter01,
    chapter02,
    chapter03,
    chapter04,
    chapter05,
    chapter06,
    chapter07,
    chapter07Twirl,
    chapter08,
    chapter09,
    chapter09Docker,
    chapter09SbtWeb,
    chapter09Standalone,
    chapter09Sbt,
    chapter10,
    chapter11Unprotected,
    chapter11Protected,
    chapter12,
    chapter13
  )
//  .settings(
//    publish := {},
//    publishLocal := {}
//  )

lazy val chapter01 = ProjectRef(file("chapter01"), "my-scalatra-web-app")
lazy val chapter02 = ProjectRef(file("chapter02"), "scalatra-cms")
lazy val chapter03 = ProjectRef(file("chapter03"), "Chapter3")
lazy val chapter04 = ProjectRef(file("chapter04"), "hacker-tracker")
lazy val chapter05 = ProjectRef(file("chapter05"), "Chapter5")
lazy val chapter06 = ProjectRef(file("chapter06"), "Chapter6")
lazy val chapter07 = ProjectRef(file("chapter07"), "Chapter7")
lazy val chapter07Twirl = ProjectRef(file("chapter07-twirl"), "Chapter7Twirl")
lazy val chapter08 = ProjectRef(file("chapter08"), "Chapter8")
lazy val chapter09 = ProjectRef(file("chapter09"), "chapter09")
lazy val chapter09Docker = ProjectRef(file("chapter09-docker"), "chapter09-docker")
lazy val chapter09SbtWeb = ProjectRef(file("chapter09-sbtweb"), "chapter09-sbtweb")
lazy val chapter09Standalone = ProjectRef(file("chapter09-standalone"), "chapter09-standalone")
lazy val chapter09Sbt = ProjectRef(file("chapter09-sbt"), "chapter09-sbt")
lazy val chapter10 = ProjectRef(file("chapter10"), "chapter10")
lazy val chapter11Unprotected = ProjectRef(file("chapter11/1-hacker-tracker-unprotected"), "hacker-tracker-unprotected")
lazy val chapter11Protected = ProjectRef(file("chapter11/2-hacker-tracker-protected"), "hacker-tracker-protected")
lazy val chapter12 = ProjectRef(file("chapter12"), "chapter-12-crawler")
lazy val chapter13 = ProjectRef(file("chapter13"), "hacker-tracker-api")

