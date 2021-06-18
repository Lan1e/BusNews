package com.example.busnews

import androidx.lifecycle.ViewModel
import com.example.busnews.api.BusAPIHelper
import com.example.busnews.api.ApiVo
import com.example.busnews.data.ResultPack
import com.example.busnews.database.BusDatabase
import com.google.gson.Gson

object BusDataManager : ViewModel() {
    interface DataUpdateListener {
        fun onTownUpdate(newTown: ArrayList<String>)
        fun onRouteUpdate(newRoute: ArrayList<String>)
        fun onStopUpdate(newStop: ArrayList<String>)
        fun onResultUpdate(newResult: ArrayList<ResultPack>)
    }

    private val dataUpdateListeners = ArrayList<DataUpdateListener>()
    private val apiHelper = BusAPIHelper()

    private val towns = ArrayList<String>().apply {
        App.context.resources.getStringArray(R.array.towns_values).forEach {
            add(it)
        }
    }
    private val routes = ArrayList<String>().apply {
        App.context.resources.getStringArray(R.array.routes_values).forEach {
            add(it)
        }
    }
    private val stops = ArrayList<String>().apply {
        App.context.resources.getStringArray(R.array.stop_values).forEach {
            add(it)
        }
    }
    private val result = ArrayList<ResultPack>()

    var isDataInitialized = false
        private set

    fun initData() {
        // region data init

        // endregion
        isDataInitialized = true
        dataUpdateListeners.forEach {
            it.onTownUpdate(towns)
            it.onRouteUpdate(routes)
            it.onStopUpdate(stops)
        }
    }

    fun updateResult() {
        apiHelper.fetchBusDelayByStop(
            onFailure = {

            },
            onResponse = { response ->
                result.clear()
                result.addAll(getResultFromVo(response))
                dataUpdateListeners.forEach {
                    it.onResultUpdate(result)
                }
            }
        )
    }

    fun updateRoute() {
        apiHelper.fetchAllRouteByDownTown(
            onResponse = { response ->
                routes

            }
        )
    }

    private fun doIfRouteNotExist(downTown: String, listener: () -> Unit) {
        if (BusDatabase().getRoomDao().searchRouteByDownTown(downTown).isEmpty()) {
            listener.invoke()
        }
    }

    private fun getResultFromVo(response: String?): List<ResultPack> {
        // need update
        return response?.let {
            Gson().fromJson(it, ApiVo::class.java)
        }?.list?.map {
            ResultPack()
        } ?: ArrayList()
    }

    fun addDataUpdateListener(listener: DataUpdateListener) {
        if (!dataUpdateListeners.contains(listener)) {
            dataUpdateListeners.add(listener)
        }
    }

    fun removeDataUpdateListener(listener: DataUpdateListener) =
        dataUpdateListeners.remove(listener)
}