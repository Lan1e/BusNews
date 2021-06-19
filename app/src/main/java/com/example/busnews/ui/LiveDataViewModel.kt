package com.example.busnews.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.busnews.BusDataManager
import com.example.busnews.data.BusInfoModel

class LiveDataViewModel : ViewModel() {
    val downTown = MutableLiveData<ArrayList<String>>().apply {
        value = ArrayList()
    }
    val route = MutableLiveData<ArrayList<String>>().apply {
        value = ArrayList()
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

            override fun onRouteUpdate(newRoute: List<String>) {
                route.value = ArrayList<String>().apply{
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