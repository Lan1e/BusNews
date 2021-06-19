package com.example.busnews

import android.content.Context
import com.example.busnews.api.BusAPIHelper
import com.example.busnews.data.BusInfoModel
import com.example.busnews.data.RouteInfoModel
import com.example.busnews.database.BusDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object BusDataManager {
    interface DataUpdateListener {
        fun onTownUpdate(newTown: List<String>)
        fun onRouteUpdate(newRoute: List<String>)
        fun onStopUpdate(newStop: List<String>)
        fun onResultUpdate(newResult: List<BusInfoModel>)
    }

    private val sharedPref =
        App.context.getSharedPreferences("com.example.aqiinfo_preferences", Context.MODE_PRIVATE)

    private val currentDownTown
        get() = sharedPref.getString("filter_option_town", "") ?: ""

    private val currentRoute
        get() = sharedPref.getString("filter_option_route", "") ?: ""

    private val currentStop
        get() = sharedPref.getString("filter_option_stop", "") ?: ""

    private val dataUpdateListeners = ArrayList<DataUpdateListener>()
    private val apiHelper = BusAPIHelper()

    private val towns = ArrayList<String>().apply {
        App.context.resources.getStringArray(R.array.towns_values).forEach {
            add(it)
        }
    }
    private val routes = ArrayList<String>()
    private val stops = ArrayList<String>()
    private val result = ArrayList<BusInfoModel>()

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
            downTown = currentDownTown,
            route = currentRoute,
            stop = currentStop,
            onFailure = {

            },
            onResponse = { results ->
                this.result.clear()
                this.result.addAll(results)
                GlobalScope.launch(Dispatchers.Main) {
                    dataUpdateListeners.forEach {
                        it.onResultUpdate(results)
                    }
                }
            }
        )
    }

    fun updateRoute() {
        getRouteFromDatabase(currentDownTown) {
            if (it.isNotEmpty()) {
                updateRouteByDB(it)
            } else {
                updateRouteByAPI(currentDownTown)
            }
        }
    }

    private fun updateRouteByAPI(downTown: String) {
        apiHelper.fetchAllRouteByDownTown(
            downTown = downTown,
            onResponse = { response ->
                this.routes.apply {
                    clear()
                    response.map {
                        it.name
                    }.let { routes ->
                        addAll(routes)
                        GlobalScope.launch(Dispatchers.Main) {
                            dataUpdateListeners.forEach {
                                it.onRouteUpdate(routes)
                            }
                        }
                    }
                }
            }
        )
    }

    private fun updateRouteByDB(routes: List<String>) =
        this.routes.apply {
            clear()
            addAll(routes)
            GlobalScope.launch(Dispatchers.Main) {
                dataUpdateListeners.forEach {
                    it.onRouteUpdate(routes)
                }
            }
        }


    private fun getRouteFromDatabase(
        downTown: String,
        listener: (List<String>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            BusDatabase(App.context).getRoomDao().searchRouteByDownTown(downTown).let {
                withContext(Dispatchers.Main) {
                    listener.invoke(it)
                }
            }
        }
    }

    fun updateStop() {
        getStopFromDataBase(currentDownTown, currentRoute) {
            if (it.isNotEmpty()) {
                updateStopByDB(it)
            } else {
                updateStopByAPI(currentDownTown, currentRoute)
            }
        }
    }

    private fun updateStopByAPI(downTown: String, route: String) {
        apiHelper.fetchAllStopsInOrderByRoute(
            downTown = downTown,
            route = route,
            onResponse = { response ->
                this.stops.apply {
                    clear()
                    response.map {
                        it.name
                    }.let { stops ->
                        addAll(stops)
                        GlobalScope.launch(Dispatchers.Main) {
                            dataUpdateListeners.forEach {
                                it.onStopUpdate(stops)
                            }
                        }
                    }
                }
            }
        )
    }

    private fun updateStopByDB(stops: List<String>) {
        this.stops.apply {
            clear()
            addAll(stops)
            GlobalScope.launch(Dispatchers.Main) {
                dataUpdateListeners.forEach {
                    it.onStopUpdate(stops)
                }
            }
        }
    }

    private fun getStopFromDataBase(
        downTown: String,
        route: String,
        listener: (List<String>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            BusDatabase(App.context).getRoomDao().searchStop(downTown, route).let {
                withContext(Dispatchers.Main) {
                    listener.invoke(it)
                }
            }
        }
    }

    fun addDataUpdateListener(listener: DataUpdateListener) {
        if (!dataUpdateListeners.contains(listener)) {
            dataUpdateListeners.add(listener)
        }
    }

    fun removeDataUpdateListener(listener: DataUpdateListener) =
        dataUpdateListeners.remove(listener)
}