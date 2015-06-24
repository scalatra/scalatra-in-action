package com.constructiveproof.hackertracker

import org.scalatra.ScalatraServlet

class GateController extends ScalatraServlet {

  before() {
    if (params("name") == "Arthur") {
      halt(status = 403,
        reason = "Forbidden",
        headers = Map("X-Your-Mother-Was-A" -> "hamster",
          "X-And-Your-Father-Smelt-Of" -> "Elderberries"),
        body = <h1>Go away or I shall taunt you a second time!</h1>)
    }
  }

  get("/") {
    "the holy grail!"
  }
}
