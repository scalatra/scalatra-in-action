package com.constructiveproof.hackertracker

import com.constructiveproof.hackertracker.models.{Hacker}
import com.constructiveproof.hackertracker.auth.AuthenticationSupport


class SessionsController extends HackerTrackerStack with AuthenticationSupport {

  before("/new") {
    logger.info("SessionsController: checking whether to run RememberMeStrategy: " + !isAuthenticated)

    if(!isAuthenticated) {
      scentry.authenticate("RememberMe")
    }
  }

  get("/new") {
    if (isAuthenticated) redirect("/hackers")

    contentType="text/html"
    ssp("/sessions/new", "allHackers" -> Hacker.all, "authenticated" -> isAuthenticated)
  }

  post("/") {
    scentry.authenticate()

    if (isAuthenticated) {
      redirect("/hackers")
    }else{
      redirect("/sessions/new")
    }
  }

  /**
   * Any action that has side-effects on the server should not be a GET (a DELETE would
   * be preferable here), but I'm going to make this a GET in order to avoid starting a discussion
   * of unobtrusive JavaScript and the creation of DELETE links at this point.
   */
  get("/destroy") {
    scentry.logout()
    redirect("/hackers")
  }

}
