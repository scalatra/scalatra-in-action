package org.scalatra.book.chapter08

import org.scalatra._

class NukeLauncherServlet(launcher: NukeLauncher) extends ScalatraServlet {
  val NuclearCode = "password123"

  post("/launch") {
    if (params("code") == NuclearCode)
      launcher.launch()
    else
      Forbidden()
  }
}

trait NukeLauncher {
  def launch(): Unit
}

object RealNukeLauncher extends NukeLauncher {
  def launch(): Unit = ???
}

class StubNukeLauncher extends NukeLauncher {
  var isLaunched = false
  def launch(): Unit = isLaunched = true
}
