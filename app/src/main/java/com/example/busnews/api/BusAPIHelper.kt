package com.example.busnews.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BusAPIHelper {
    companion object {
        private const val API_URL_PREFIX = "http://ptx.transportdata.tw/MOTC/APIs"
        private const val APP_ID = "e9b441ad9436493192f186928046a681"
        private const val APP_Key = "2At3NPAnTDRwQZoCyhvCP0J4YWs"
    }

    private val currentDateTime get() = LocalDateTime.now()
    private val xDate get() = currentDateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME)
    private val signDate get() = "x-date: $xDate"

    fun fetchAllRouteByDownTown(
        onFailure: (() -> Unit)? = null,
        onResponse: ((String?) -> Unit)? = null
    ) {
        val apiUrl = API_URL_PREFIX + ""
        sendRequest(apiUrl, onFailure, onResponse)
    }

    fun fetchAllStopsByRoute(
        onFailure: (() -> Unit)? = null,
        onResponse: ((String?) -> Unit)? = null
    ) {
        val apiUrl = API_URL_PREFIX + ""
        sendRequest(apiUrl, onFailure, onResponse)
    }

    fun fetchBusDelayByStop(
        onFailure: (() -> Unit)? = null,
        onResponse: ((String?) -> Unit)? = null
    ) {
        val apiUrl = API_URL_PREFIX + ""
        sendRequest(apiUrl, onFailure, onResponse)
    }

    private fun sendRequest(
        url: String,
        onFailure: (() -> Unit)? = null,
        onResponse: ((String?) -> Unit)? = null
    ) {
        Signature().createSignature(signDate, APP_Key).let { signature: String ->
            val sAuth =
                "hmac username=\"$APP_ID\", algorithm=\"hmac-sha1\", headers=\"x-date\", signature=\"$signature\""
            val req = Request.Builder()
                .header("Authorization", sAuth)
                .header("x-date", xDate)
                .url(url)
                .build()

            OkHttpClient().newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    GlobalScope.launch(Dispatchers.Main) {
                        onFailure?.invoke()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    GlobalScope.launch(Dispatchers.Main) {
                        onResponse?.invoke(response.body?.string())
                    }
                }
            })
        }
    }


}