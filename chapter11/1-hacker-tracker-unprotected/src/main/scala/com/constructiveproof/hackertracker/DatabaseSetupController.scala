package com.constructiveproof.hackertracker

import com.constructiveproof.hackertracker.models.Db

class DatabaseSetupController extends HackerTrackerStack {


  before() {
    contentType = "text/html"
  }

  /**
   * Create a new database.
   */
  get("/create") {
    Db.init
    flash("notice") = "Database created"
    redirect("/hackers/new")
  }

  /** *
    * Print the schema definition for our database out to the console.
    */
  get("/dump-schema") {
    Db.printDdl
    "Take a look in your running console and you'll see the data definition list printout."
  }

  /**
   * Whack the database.
   */
  get("/drop") {
    Db.drop
    "The database has been dropped."
  }
}
