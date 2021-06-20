package com.example.busnews

import android.content.Context
import android.util.Log
import android.widget.Toast
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
        fun onRouteUpdate(newRoute: List<RouteInfoModel>)
        fun onStopUpdate(newStop: List<String>)
        fun onResultUpdate(newResult: List<BusInfoModel>)
    }

    private val sharedPref =
        App.context.getSharedPreferences("com.example.aqiinfo_preferences", Context.MODE_PRIVATE)

    var currentDownTown = sharedPref.getString("filter_option_town", "") ?: ""

    var currentSubRoute = sharedPref.getString("filter_option_route", "") ?: ""

    var currentStop = sharedPref.getString("filter_option_stop", "") ?: ""

    private val dataUpdateListeners = ArrayList<DataUpdateListener>()
    private val apiHelper = BusAPIHelper()

    private val towns = ArrayList<String>().apply {
        App.context.resources.getStringArray(R.array.towns_values).forEach {
            add(it)
        }
    }
    private val routes = ArrayList<RouteInfoModel>()
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
        routes.find {
            it.subRoute == currentSubRoute
        }?.let {
            apiHelper.fetchBusDelayByStop(
                downTown = currentDownTown,
                mainRoute = it.mainRoute,
                subRoute = it.subRoute,
                stop = currentStop,
                onFailure = {

                },
                onResponse = { results ->
                    result.clear()
                    result.addAll(results)
                    GlobalScope.launch(Dispatchers.Main) {
                        dataUpdateListeners.forEach {
                            it.onResultUpdate(results)
                        }
                    }
                }
            )
        }
    }

    fun updateRoute() {
        getRouteFromDatabase(currentDownTown) {
            if (it.isNotEmpty()) {
                updateRouteByDB(it)
                Log.i("lanie", "update route by db")
            } else {
                updateRouteByAPI(currentDownTown)
                Log.i("lanie", "update route by api")
            }
        }
    }

    private fun updateRouteByAPI(downTown: String) {
        apiHelper.fetchAllRouteByDownTown(
            downTown = downTown,
            onResponse = { response ->
                this.routes.apply {
                    clear()
                    response.let { routes ->
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

    private fun updateRouteByDB(routes: List<RouteInfoModel>) =
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
        listener: (List<RouteInfoModel>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            BusDatabase(App.context).getRoomDao().searchRouteByDownTown(downTown).let {
                Log.i("lanie", "getRouteFromDatabase")
                withContext(Dispatchers.Main) {
                    listener.invoke(it)
                }
            }
        }
    }

    fun updateStop() {
        getStopFromDataBase(currentDownTown, currentSubRoute) {
            if (it.isNotEmpty()) {
                updateStopByDB(it)
                Log.i("lanie", "update stop by db")
            } else {
                routes.find {
                    it.subRoute == currentSubRoute
                }?.apply {
                    updateStopByAPI(currentDownTown, mainRoute, subRoute)
                    Log.i("lanie", "update stop by api")
                }
            }
        }
    }

    private fun updateStopByAPI(downTown: String, mainRoute: String, subRoute: String) {
        apiHelper.fetchAllStopsInOrderByRoute(
            downTown = downTown,
            mainRoute = mainRoute,
            subRoute = subRoute,
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