package com.example.busnews

import okhttp3.Request
import java.security.SignatureException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BusAPIHelper {
    val APIUrl = "http://ptx.transportdata.tw/MOTC/APIs"
    val APPID = "e9b441ad9436493192f186928046a681"
    val APPKey = "2At3NPAnTDRwQZoCyhvCP0J4YWs"
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.RFC_1123_DATE_TIME


    private fun getCurrentLocationOfTheBus() {

    }



    private fun getData(url: String) {
        Signature().createSignature(signDate, APPKey).apply { signature: String ->
            val sAuth =
                "hmac username=\"$APPID\", algorithm=\"hmac-sha1\", headers=\"x-date\", signature=\"$signature\""
            val req = Request.Builder()
                .header("Authorization", sAuth)
                .header("x-date", xDate)
                .url(url)
                .build()

        }
    }
    val xDate = current.format(formatter)
    val signDate = "x-date: $xDate"


}