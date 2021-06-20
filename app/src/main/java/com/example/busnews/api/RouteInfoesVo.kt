package com.example.busnews.api

class RoutesInfoVo(

    private val _list: List<RouteInfoVo>?
) {
    val list get() = _list?:ArrayList()
    class RouteInfoVo(
        val RouteName:RouteInfoVo,
        val SubRouteName: SubRouteInfoVo
    ) {
        class RouteInfoVo(val Zh_tw: String)
        class SubRouteInfoVo(val Zh_tw: String)
    }
}