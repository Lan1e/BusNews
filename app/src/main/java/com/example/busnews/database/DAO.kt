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

    @Query("SELECT ${DownTownRouteEntity.ROUTE} FROM ${DownTownRouteEntity.TABLE_NAME} WHERE ${DownTownRouteEntity.DOWN_TOWN} LIKE :downTown")
    fun searchRouteByDownTown(downTown: String): List<String>

    @Query("SELECT ${RouteStopEntity.STOP} FROM ${RouteStopEntity.TABLE_NAME} INNER JOIN ${DownTownRouteEntity.TABLE_NAME} ON ${RouteStopEntity.TABLE_NAME}.${RouteStopEntity.ROUTE} = ${DownTownRouteEntity.TABLE_NAME}.${DownTownRouteEntity.ROUTE} WHERE  ${DownTownRouteEntity.TABLE_NAME}.${DownTownRouteEntity.DOWN_TOWN} =:downTown AND  ${DownTownRouteEntity.TABLE_NAME}.${RouteStopEntity.ROUTE} =:route")
    fun searchStop(downTown: String, route: String): List<String>
}