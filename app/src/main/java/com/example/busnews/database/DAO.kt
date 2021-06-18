package com.example.busnews.database

import androidx.room.*

@Dao
interface DAO {
    @Insert
    fun insert(item: DownTownRouteEntity): Long

    @Delete
    fun delete(item: DownTownRouteEntity)

    @Update
    fun update(item: DownTownRouteEntity)

    @Query("SELECT ${DownTownRouteEntity.ROUTE} FROM ${DownTownRouteEntity.TABLE_NAME} WHERE ${DownTownRouteEntity.DOWN_TOWN} = :downTown")
    fun searchRouteByDownTown(downTown: String): List<String>

    @Query("SELECT ${RouteStopEntity.STOP} FROM ${RouteStopEntity.TABLE_NAME} INNER JOIN ${DownTownRouteEntity.TABLE_NAME} ON ${RouteStopEntity.ROUTE} = :${DownTownRouteEntity.ROUTE}")
    fun searchStopByRoute(route: String): List<String>
}