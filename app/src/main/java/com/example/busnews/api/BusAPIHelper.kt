package com.example.busnews.api

import SslUtils
import android.util.Log
import com.example.busnews.App
import com.example.busnews.data.BusInfoModel
import com.example.busnews.data.RouteInfoModel
import com.example.busnews.data.RouteStopInfoModel
import com.example.busnews.database.BusDatabase
import com.example.busnews.database.RouteInfoEntity
import com.example.busnews.database.RouteStopEntity
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.security.SignatureException
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.GZIPInputStream


class BusAPIHelper {
    companion object {
        private const val API_URL_PREFIX = "https://ptx.transportdata.tw/MOTC"
        private const val APP_ID = "e9b441ad9436493192f186928046a681"
        private const val APP_Key = "2At3NPAnTDRwQZoCyhvCP0J4YWs"
    }

    private val signDate get() = "x-date: $xDate"
    private var connection: HttpURLConnection? = null
    private val xDate: String
        get() {
            val calendar: Calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"))
            return dateFormat.format(calendar.getTime())
        }


    fun fetchAllRouteByDownTown(
        downTown: String,
        onFailure: (() -> Unit)? = null,
        onResponse: ((List<RouteInfoModel>) -> Unit)? = null
    ) {
        val apiUrl = "$API_URL_PREFIX/v2/Bus/Shape/City/$downTown" +
                "?$${ApiParam("select", "SubRouteName, RouteName")}" +
                "&$${ApiParam("filter", "Direction ne '1'")}" +
                "&$${ApiParam("top", "500")}" +
                "&$${ApiParam("format", "JSON")}"
        sendRequest(apiUrl, onFailure) {
            Gson().fromJson<Array<RoutesInfoVo.RouteInfoVo>>(
                it,
                Array<RoutesInfoVo.RouteInfoVo>::class.java
            ).map {
                RouteInfoModel(it.RouteName.Zh_tw, it.SubRouteName.Zh_tw)
            }.let {
                it.map {
                    RouteInfoEntity(downTown, it.mainRoute, it.subRoute)
                }.forEach {
                    BusDatabase(App.context).getRoomDao().insert(it)
                }
                onResponse?.invoke(it)
                Log.e("lanie", "${it.size} AllRoute")
            }
        }
    }

    fun fetchAllStopsInOrderByRoute(
        downTown: String,
        mainRoute:String,
        subRoute: String,
        onFailure: (() -> Unit)? = null,
        onResponse: ((List<RouteStopInfoModel>) -> Unit)? = null
    ) {
        val apiUrl = "$API_URL_PREFIX/v2/Bus/StopOfRoute/City/$downTown/$mainRoute" +
                "?$${ApiParam("filter", "Direction ne '1' and SubRouteName/Zh_tw eq '$subRoute'")}" +
                "&$${ApiParam("top", "500")}" +
                "&$${ApiParam("format", "JSON")}"
        sendRequest(apiUrl, onFailure) {
            Gson().fromJson<Array<StopsOfRouteInfoVo>>(it, Array<StopsOfRouteInfoVo>::class.java)
                .first().let {
                    it.Stops.map {
                        RouteStopInfoModel(name = it.StopName.Zh_tw)
                    }
                }.let {
                    it.map {
                        RouteStopEntity(subRoute, it.name)
                    }.forEach {
                        BusDatabase(App.context).getRoomDao().insert(it)
                    }
                    onResponse?.invoke(it)
                    Log.e("lanie", "${it.size} AllStopsInOrder")
                }
        }
    }

    fun fetchBusDelayByStop(
        downTown: String,
        mainRoute:String,
        subRoute: String,
        stop: String,
        onFailure: (() -> Unit)? = null,
        onResponse: ((List<BusInfoModel>) -> Unit)? = null
    ) {
        val apiUrl = "$API_URL_PREFIX/v2/Bus/EstimatedTimeOfArrival/City/$downTown/$mainRoute" +
                "?$${ApiParam("select", "EstimateTime")}" +
                "&$${
                    ApiParam(
                        "filter",
                        "PlateNumb ne '' and PlateNumb ne '-1' and StopName/Zh_tw eq '$stop' and SubRouteName/Zh_tw eq '$subRoute'"
                    )
                }" +
                "&$${ApiParam("top", "500")}" +
                "&$${ApiParam("format", "JSON")}"
        sendRequest(apiUrl, onFailure) {
            Gson().fromJson<Array<StopsInfoVo>>(it, Array<StopsInfoVo>::class.java).map {
                BusInfoModel(it.EstimateTime, it.PlateNumb)
            }.let {
                onResponse?.invoke(it)
                Log.e("lanie", "${it.size} BusDelay")
            }
        }
    }

    fun fetchCurrentBusPositionByRoute(
        downTown: String,
        plateNum:String,
        route: String,
        onFailure: (() -> Unit)? = null,
        onResponse: ((List<RouteStopInfoModel>) -> Unit)? = null
    ) {
        val apiUrl = "$API_URL_PREFIX/v2/Bus/RealTimeNearStop/City/$downTown/$route" +
                "?$${ApiParam("select", "StopName")}" +
                "&$${ApiParam("filter", "PlateNumb eq '$plateNum'")}" +
                "&$${ApiParam("top", "500")}" +
                "&$${ApiParam("format", "JSON")}"
        sendRequest(apiUrl, onFailure) {
            Gson().fromJson<Array<BusesInfoVo>>(it, Array<BusesInfoVo>::class.java).map {
                RouteStopInfoModel(it.PlateNumb, it.StopName.Zh_tw)
            }.let {
                onResponse?.invoke(it)
                Log.e("lanie", "${it.size} CurrentBusPosition")
            }
        }
    }

    private fun sendRequest(
        url: String,
        onFailure: (() -> Unit)? = null,
        onResponse: ((JsonReader) -> Unit)? = null
    ) {
        Log.i("lanie", "send url: $url")
        var Signature = ""
        try {
            Signature = HMAC_SHA1.Signature(signDate, APP_Key)
        } catch (e1: SignatureException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }

        val sAuth =
            "hmac username=\"$APP_ID\", algorithm=\"hmac-sha1\", headers=\"x-date\", signature=\"$Signature\""
        val url = URL(url)
        if ("https".equals(url.getProtocol(), ignoreCase = true)) {
            SslUtils.ignoreSsl()
        }
        connection = (url.openConnection() as? HttpURLConnection)?.apply {
            requestMethod = "GET"
            setRequestProperty("Authorization", sAuth)
            setRequestProperty("x-date", xDate)
            setRequestProperty("Accept-Encoding", "gzip")
            doInput = true
//            doOutput = true
        }


        GlobalScope.launch(Dispatchers.IO) {
            var respond = ""
            respond = connection!!.responseCode.toString() + " " + connection!!.responseMessage
            Log.i("lanie", "$respond")

            try {
                val reader = if ("gzip" == connection!!.contentEncoding) {
                    JsonReader(InputStreamReader(GZIPInputStream(connection!!.inputStream)))
                } else {
                    JsonReader(InputStreamReader(connection!!.inputStream))
                }
                reader.isLenient = true
                onResponse?.invoke(reader)
            } catch (e: Exception) {
                Log.e("lanie", "wewe", e)
            }
        }
    }
}