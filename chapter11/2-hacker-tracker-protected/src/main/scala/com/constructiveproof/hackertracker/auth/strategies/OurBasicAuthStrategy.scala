package com.constructiveproof.hackertracker.auth.strategies

import org.scalatra.auth.strategy.{BasicAuthStrategy}
import org.scalatra.{ScalatraBase}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.constructiveproof.hackertracker.models.User

class OurBasicAuthStrategy(protected override val app: ScalatraBase, realm: String)
  extends BasicAuthStrategy[User](app, realm) {

  protected def validate(username: String, password: String)(implicit request: HttpServletRequest, response: HttpServletResponse): Option[User] = {
    if(username == "scalatra" && password == "scalatra") Some(User("scalatra"))
    else None
  }

  protected def getUserId(user: User)(implicit request: HttpServletRequest, response: HttpServletResponse): String = user.id
}


