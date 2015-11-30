package com.constructiveproof.hackertracker.auth.strategies

import org.scalatra.ScalatraBase
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.scalatra.auth.ScentryStrategy
import com.constructiveproof.hackertracker.models.User
import org.slf4j.LoggerFactory


class UserPasswordStrategy(protected val app: ScalatraBase)
  (implicit request: HttpServletRequest, response: HttpServletResponse)
  extends ScentryStrategy[User] {

  override def name: String = "UserPassword"

  val logger = LoggerFactory.getLogger(getClass)

  private def username = app.params.getOrElse("username", "")
  private def password = app.params.getOrElse("password", "")

  /***
    * Determine whether the strategy should be run for the current request.
    */
  override def isValid(implicit request: HttpServletRequest) = {
    logger.info("UserPasswordStrategy: determining isValid: " + (username != "" && password != "").toString())
    username != "" && password != ""
  }


  /**
   *  In real life, this is where we'd consult our data store, asking it whether the user credentials matched
   *  any existing user. Here, we'll just check for a known username/password combination and return a user if
   *  it's found.
   */
  def authenticate()(implicit request: HttpServletRequest, response: HttpServletResponse): Option[User] = {
    logger.info("UserPasswordStrategy: attempting authentication")

    if(username == "foo" && password == "foo") {
      logger.info("UserPasswordStrategy: username succeeded")
      Some(User("foo"))
    } else {
      logger.info("UserPasswordStrategy: username failed")
      None
    }
  }

  /**
   * What should happen if the user is currently not authenticated?
   */
  override def unauthenticated()(implicit request: HttpServletRequest, response: HttpServletResponse) {
    app.redirect("/sessions/new")
  }
}