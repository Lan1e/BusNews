package com.example.busnews.data

class RouteInfoModel(
    val mainRoute: String,
    val subRoute: String
) {
    fun getRouteName() =
        if (subRoute.isEmpty()) mainRoute else subRoute
}