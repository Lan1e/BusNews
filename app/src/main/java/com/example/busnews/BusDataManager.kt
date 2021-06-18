package com.example.busnews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BusDataManager : ViewModel() {
    val downTown = MutableLiveData<String?>()
    val route = MutableLiveData<String?>()
    val stop = MutableLiveData<String?>()


}