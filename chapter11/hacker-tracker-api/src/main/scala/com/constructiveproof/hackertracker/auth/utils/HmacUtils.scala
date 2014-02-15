package com.constructiveproof.hackertracker.auth.utils

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import sun.misc.BASE64Encoder

object HmacUtils {

  def verify(secretKey: String, stringToVerify: String, hmac: String): Boolean = {
    signature(secretKey, stringToVerify) == hmac
  }

  private def signature(secretKey: String, stringToSign: String): String = {
    val secret = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1")
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(secret)
    val hmac = mac.doFinal(stringToSign.getBytes)
    new BASE64Encoder().encode(hmac)
  }

}
