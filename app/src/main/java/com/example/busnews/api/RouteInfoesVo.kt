package com.example.busnews.api

class RoutesInfoVo(

    private val _list: List<RouteInfoVo>?
) {
    val list get() = _list?:ArrayList()
    class RouteInfoVo(
        val SubRouteName: SubRouteInfoVo
    ) {
        class SubRouteInfoVo(val Zh_tw: String)
    }
}