package com.example.busnews.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.busnews.BusDataManager
import com.example.busnews.data.BusInfoModel
import com.example.busnews.data.RouteInfoModel

class LiveDataViewModel : ViewModel() {
    val downTown = MutableLiveData<ArrayList<String>>().apply {
        value = ArrayList()
    }
    val route = MutableLiveData<ArrayList<RouteInfoModel>>().apply {

    }
    val stop = MutableLiveData<ArrayList<String>>().apply {
        value = ArrayList()
    }
    val result = MutableLiveData<ArrayList<BusInfoModel>>().apply {
        value = ArrayList()
    }

    init {
        BusDataManager.addDataUpdateListener(object : BusDataManager.DataUpdateListener {
            override fun onTownUpdate(newTown: List<String>) {
                downTown.value = ArrayList<String>().apply{
                    addAll(newTown)
                }
            }

            override fun onRouteUpdate(newRoute: List<RouteInfoModel>) {
                route.value = ArrayList<RouteInfoModel>().apply{
                    addAll(newRoute)
                }
            }

            override fun onStopUpdate(newStop: List<String>) {
                stop.value = ArrayList<String>().apply{
                    addAll(newStop)
                }
            }

            override fun onResultUpdate(newResult: List<BusInfoModel>) {
                result.value = ArrayList<BusInfoModel>().apply{
                    addAll(newResult)
                }
            }
        })
        BusDataManager.initData()
    }
}