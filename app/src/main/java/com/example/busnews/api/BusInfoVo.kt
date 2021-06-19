package com.example.busnews.api

class BusesInfoVo(
    val PlateNumb: String,
    val StopName: BusPosition
) {
    class BusPosition(
        val Zh_tw: String
    )
}