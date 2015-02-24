package com.constructiveproof.hackertracker.auth.utils

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import sun.misc.BASE64Encoder

object HmacUtils {

  def verify(secretKey: String, signMe: String, hmac: String): Boolean = {
    sign(secretKey, signMe) == hmac
  }

  def sign(secretKey: String, signMe: String): String = {
    val secret = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1")
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(secret)
    val hmac = mac.doFinal(signMe.getBytes)
    new BASE64Encoder().encode(hmac)
  }

}
