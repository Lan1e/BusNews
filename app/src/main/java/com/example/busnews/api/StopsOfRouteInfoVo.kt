package com.example.busnews.api

class StopsOfRouteInfoVo(
    val Stops: List<StopOfRouteInfoVo>
) {
    class StopOfRouteInfoVo(
        val StopName: StopNameVo
    ) {
        class StopNameVo(
            val Zh_tw: String
        )
    }
}