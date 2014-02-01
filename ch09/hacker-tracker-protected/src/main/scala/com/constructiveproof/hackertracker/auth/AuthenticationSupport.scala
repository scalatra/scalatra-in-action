package com.constructiveproof.hackertracker.auth

import org.scalatra.ScalatraBase
import org.scalatra.auth.{ScentryConfig, ScentrySupport}
import com.constructiveproof.hackertracker.auth.strategies.UserPasswordStrategy
import com.constructiveproof.hackertracker.models.User
import com.constructiveproof.hackertracker.auth.strategies.RememberMeStrategy
import org.slf4j.LoggerFactory

trait AuthenticationSupport extends ScalatraBase with ScentrySupport[User] {
  self: ScalatraBase =>

  val logger = LoggerFactory.getLogger(getClass)


  protected val scentryConfig = (new ScentryConfig {}).asInstanceOf[ScentryConfiguration]

  protected def fromSession = { case id: String => User(id)  }
  protected def toSession   = { case usr: User => usr.id }

  protected def requireLogin() = {
    if(!isAuthenticated) {
      redirect("/sessions/new")
    }
  }


  override protected def configureScentry = {
    scentry.unauthenticated {
      scentry.strategies("UserPassword").unauthenticated()
    }
  }

  override protected def registerAuthStrategies = {
    scentry.register("UserPassword", app => new UserPasswordStrategy(app))
    scentry.register("RememberMe", app => new RememberMeStrategy(app))
  }

}