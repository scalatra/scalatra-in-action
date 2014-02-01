package com.constructiveproof.hackertracker.auth

import org.scalatra.auth.{ScentryConfig, ScentrySupport}
import org.scalatra.auth.strategy.BasicAuthSupport
import org.scalatra.ScalatraBase
import com.constructiveproof.hackertracker.auth.strategies.OurBasicAuthStrategy
import com.constructiveproof.hackertracker.models.User

/**
 * Mix this trait into any controller and it'll require HTTP Basic Authentication using the OurBasicAuthStrategy.
 *
 * To protect individual routes, just add "basicAuth" at the top of the action, or use a before() filter to protect the whole controller.
 */
trait OurBasicAuthenticationSupport extends ScentrySupport[User] with BasicAuthSupport[User] {
  self: ScalatraBase =>

  val realm = "Scalatra Basic Auth Example"

  protected def fromSession = { case id: String => User(id)  }
  protected def toSession   = { case usr: User => usr.id }

  protected val scentryConfig = (new ScentryConfig {}).asInstanceOf[ScentryConfiguration]


  override protected def configureScentry = {
    scentry.unauthenticated {
      scentry.strategies("Basic").unauthenticated()
    }
  }

  override protected def registerAuthStrategies = {
    scentry.register("Basic", app => new OurBasicAuthStrategy(app, realm))
  }

}


