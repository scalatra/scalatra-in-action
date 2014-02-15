package com.constructiveproof.hackertracker.auth

import com.constructiveproof.hackertracker.auth.utils.HmacUtils
import org.scalatra.{ScalatraBase, Unauthorized}

trait ApiAuthenticationSupport extends  {
  self: ScalatraBase =>

  protected def validateRequest() = {
    val secretKey = "thisisthesecretkey"
    val hmac = params.getOrElse("sig", unauthorized)

    if(!HmacUtils.verify(secretKey, requestPath, hmac)){
      unauthorized
    }
  }

  protected def unauthorized = halt(Unauthorized("Please provide a valid sig parameter"))


}
