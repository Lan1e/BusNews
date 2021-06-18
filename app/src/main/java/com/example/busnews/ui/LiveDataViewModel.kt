package com.example.busnews.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.busnews.BusDataManager
import com.example.busnews.data.ResultPack

class LiveDataViewModel : ViewModel() {
    val downTown = MutableLiveData<ArrayList<String>>()
    val route = MutableLiveData<ArrayList<String>>()
    val stop = MutableLiveData<ArrayList<String>>()
    val result = MutableLiveData<ArrayList<ResultPack>>()

    init {
        BusDataManager.addDataUpdateListener(object : BusDataManager.DataUpdateListener {
            override fun onTownUpdate(newTown: ArrayList<String>) {
                downTown.value?.apply {
                    clear()
                    addAll(newTown)
                }
            }

            override fun onRouteUpdate(newRoute: ArrayList<String>) {
                route.value?.apply {
                    clear()
                    addAll(newRoute)
                }
            }

            override fun onStopUpdate(newStop: ArrayList<String>) {
                stop.value?.apply {
                    clear()
                    addAll(newStop)
                }
            }

            override fun onResultUpdate(newResult: ArrayList<ResultPack>) {
                result.value?.apply {
                    clear()
                    addAll(newResult)
                }
            }
        })
        BusDataManager.initData()
    }
}