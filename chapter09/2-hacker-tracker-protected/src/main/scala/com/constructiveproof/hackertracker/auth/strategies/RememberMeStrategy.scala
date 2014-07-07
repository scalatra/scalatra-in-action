package com.constructiveproof.hackertracker.auth.strategies

import org.scalatra.{CookieOptions, ScalatraBase}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.scalatra.auth.ScentryStrategy
import com.constructiveproof.hackertracker.models.User
import org.slf4j.LoggerFactory


class RememberMeStrategy(protected val app: ScalatraBase)(implicit request: HttpServletRequest, response: HttpServletResponse)
  extends ScentryStrategy[User] {

  override def name: String = "RememberMe"

  val logger = LoggerFactory.getLogger(getClass)

  val COOKIE_KEY = "rememberMe"
  private val oneWeek = 7 * 24 * 3600

  /***
    * Determine whether the strategy should be run for the current request.
    *
    * TODO: THIS NEEDS TO GO IN THE BOOK, IT'S NOT THERE TO THE GREAT DETRIMENT
    * OF THE EXPLANATION - IT'S IMPOSSIBLE TO LOG IN GIVEN THE CODE EXAMPLE.
    */
  override def isValid(implicit request: HttpServletRequest):Boolean = {
    logger.info("RememberMeStrategy: determining isValid: " + (tokenVal != "").toString())
    tokenVal != ""
  }

  /***
    * Grab the value of the rememberMe cookie token.
    */
  private def tokenVal = {
    app.cookies.get(COOKIE_KEY) match {
      case Some(token) => token
      case None => ""
    }
  }

  /***
    * In a real application, we'd check the cookie's token value against a known hash, probably saved in a
    * datastore, to see if we should accept the cookie's token. Here, we'll just see if it's the one we set
    * earlier ("foobar") and accept it if so.
    */
  def authenticate()(implicit request: HttpServletRequest, response: HttpServletResponse) = {
    logger.info("RememberMeStrategy: attempting authentication")
    if(tokenVal == "foobar") Some(User("foo"))
    else None
  }

  override def unauthenticated()(implicit request: HttpServletRequest, response: HttpServletResponse) {
    app.redirect("/sessions/new")
  }

  /***
    * After successfully authenticating with either the RememberMeStrategy, or the UserPasswordStrategy with the
    * "remember me" tickbox checked, we set a rememberMe cookie for later use.
    */
  override def afterAuthenticate(winningStrategy: String, user: User)(implicit request: HttpServletRequest, response: HttpServletResponse) = {
    logger.info("rememberMe: afterAuth fired")
    if (winningStrategy == "RememberMe" ||
      (winningStrategy == "UserPassword" && checkbox2boolean(app.params.get("rememberMe").getOrElse("").toString))) {

      val token = "foobar"
      app.cookies.set(COOKIE_KEY, token)(CookieOptions(maxAge = oneWeek, path = "/"))
    }
  }

  /**
   * Run this code before logout, to clean up any leftover database state and delete the rememberMe token cookie.
   */
  override def beforeLogout(user: User)(implicit request: HttpServletRequest, response: HttpServletResponse) = {
    logger.info("rememberMe: beforeLogout")
    if (user != null){
      user.forgetMe
    }
    app.cookies.delete(COOKIE_KEY)(CookieOptions(path = "/"))
  }


  /**
   * Used to easily match a checkbox value
   */
  private def checkbox2boolean(s: String): Boolean = {
    s match {
      case "yes" => true
      case "y" => true
      case "1" => true
      case "true" => true
      case _ => false
    }
  }

}
