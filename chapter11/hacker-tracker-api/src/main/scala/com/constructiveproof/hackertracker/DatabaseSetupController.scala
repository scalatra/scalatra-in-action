package com.constructiveproof.hackertracker

import com.constructiveproof.hackertracker.auth.OurBasicAuthenticationSupport
import com.constructiveproof.hackertracker.models.Db
import com.constructiveproof.hackertracker.stacks.BrowserStack

class DatabaseSetupController extends BrowserStack with OurBasicAuthenticationSupport {


  before() {
    contentType = "text/html"
    basicAuth
  }

  /**
   * Create a new database.
   */
  get("/create") {
    Db.create
    flash("notice") = "Database created!"
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
