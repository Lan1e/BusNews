package com.example.busnews.api


import android.util.Base64.encodeToString
import java.security.SignatureException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HMAC_SHA1 {
    @Throws(SignatureException::class)
    fun Signature(xData: String, AppKey: String): String {
        return try {
            val encoder = Base64.getEncoder()
            // get an hmac_sha1 key from the raw key bytes
            val signingKey = SecretKeySpec(AppKey.toByteArray(charset("UTF-8")), "HmacSHA1")

            // get an hmac_sha1 Mac instance and initialize with the signing key
            val mac = Mac.getInstance("HmacSHA1")
            mac.init(signingKey)

            // compute the hmac on input data bytes
            val rawHmac = mac.doFinal(xData.toByteArray(charset("UTF-8")))

            encoder.encodeToString(rawHmac).replace("\n", "")
        } catch (e: Exception) {
            throw SignatureException("Failed to generate HMAC : " + e.message)
        }
    }
}