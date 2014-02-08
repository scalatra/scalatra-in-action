package com.constructiveproof.hackertracker

import org.scalatra._
import com.constructiveproof.hackertracker.models.Hacker
import com.constructiveproof.hackertracker.auth.AuthenticationSupport

class HackersController extends HackerTrackerStack with AuthenticationSupport {

  before() {
    contentType = "text/html"
  }

  /**
   * Show all hackers.
   */
  get("/") {
    ssp("/hackers/index", "allHackers" -> Hacker.all, "authenticated" -> isAuthenticated)
  }

  /**
   * Show a specific hacker.
   */
  get("/:id") {
    val id = params.getAs[Int]("id").getOrElse(0)
    val hacker = Hacker.get(id)
    ssp("/hackers/show", "hacker" -> hacker, "allHackers" -> Hacker.all, "authenticated" -> isAuthenticated)
  }

  /**
   * Display a form for creating a new hacker.
   */
  get("/new") {
    requireLogin
    ssp("/hackers/new", "allHackers" -> Hacker.all, "authenticated" -> isAuthenticated)
  }


  /**
   * Create a new hacker in the database.
   */
  post("/") {
    requireLogin
    val firstName = params("firstname")
    val lastName = params("lastname")
    val motto = params("motto")
    val birthYear = params.getAs[Int]("birthyear").getOrElse(
      halt(BadRequest("Please provide a year of birth.")))

    val hacker = new Hacker(0, firstName, lastName, motto, birthYear)

    if(Hacker.create(hacker)) {
      flash("notice") = "Hacker successfully persisted."
      redirect("/hackers/" + hacker.id)
    }
  }

}

