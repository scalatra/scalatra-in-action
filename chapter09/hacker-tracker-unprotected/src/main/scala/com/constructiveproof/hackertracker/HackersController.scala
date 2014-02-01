package com.constructiveproof.hackertracker

import org.scalatra._
import com.constructiveproof.hackertracker.models.{Db, Hacker}
import org.squeryl.PrimitiveTypeMode._

class HackersController extends HackerTrackerStack {

  before() {
    contentType = "text/html"
  }

  /**
   * Show all hackers.
   */
  get("/") {
    ssp("/hackers/index", "allHackers" -> Hacker.all)
  }

  /**
   * Show a specific hacker.
   */
  get("/:id") {
    val id = params.getAs[Int]("id")

    val hacker = Db.hackers.where(h => h.id === id).single
    ssp("/hackers/show", "hacker" -> hacker, "allHackers" -> Hacker.all)
  }

  /**
   * Display a form for creating a new hacker.
   */
  get("/new") {
    ssp("/hackers/new", "allHackers" -> Hacker.all)
  }


  /**
   * Create a new hacker in the database.
   */
  post("/") {
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

