package com.constructiveproof.hackertracker.auth

import java.net.URLEncoder

import com.constructiveproof.hackertracker.auth.utils.HmacUtils
import org.scalatra.{ScalatraBase, Unauthorized}

trait ApiAuthenticationSupport extends {
  self: ScalatraBase =>

  protected val secretKey = "thisisthesecretkey"

  /**
   * Halt the current request with an Unauthorized result unless the request
   * has been properly signed.
   */
  protected def validateRequest() = {
    if (!HmacUtils.verify(secretKey, signMe, hmac)) {
      unauthorized
    }
  }

  /**
   * Pull the incoming sig off the params map
   * @return the signature parameter, a URLDecoded string
   */
  protected def hmac = params.getOrElse("sig", unauthorized)

  /**
   * Does the halting for unauthorized requests.
   */
  protected def unauthorized = {
    notifySig()
    halt(Unauthorized("Please provide a valid sig parameter"))
  }

  /**
   * Concatenates the full request path and the HTTP verb of the current request,
   * in a format suitable for HMAC signing.
   *
   * @return a String with HTTP verb and request path separated by a comma
   */
  protected def signMe = request.requestMethod + "," + request.scriptName + requestPath


  /**
   * When you first make a request, it'll be unsigned and it will fail.
   * Copy the ?sig=XXXXXXXX information out of your sbt console and append it
   * to the URL in order to sign the request.
   */
  protected def notifySig() = {
    val base64hmac = HmacUtils.sign(secretKey, signMe)
    val urlEncodedHmac = URLEncoder.encode(base64hmac, "UTF-8")
    println("Append the following to this request in order to sign it: ?sig=" + urlEncodedHmac)
  }

}
