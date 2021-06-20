package com.example.busnews.data

import android.text.format.DateUtils

data class BusInfoModel(
    private val estimateDelay: String,
    val plateNumber: String
) {
    val delayMin: Long
        get() = estimateDelay.toFloat().toLong() * DateUtils.SECOND_IN_MILLIS / DateUtils.MINUTE_IN_MILLIS
}