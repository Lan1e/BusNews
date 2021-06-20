package com.example.busnews.database

import androidx.room.*
import com.example.busnews.data.RouteInfoModel

@Dao
interface DAO {
    @Insert
    fun insert(item: RouteInfoEntity): Long

    @Insert
    fun insert(item:RouteStopEntity):Long

    @Delete
    fun delete(item: RouteInfoEntity)

    @Update
    fun update(item: RouteInfoEntity)

    @Query("SELECT ${RouteInfoEntity.MAIN_ROUTE}, ${RouteInfoEntity.SUB_ROUTE} FROM ${RouteInfoEntity.TABLE_NAME} WHERE ${RouteInfoEntity.DOWN_TOWN} LIKE :downTown")
    fun searchRouteByDownTown(downTown: String): List<RouteInfoModel>

    @Query("SELECT ${RouteStopEntity.STOP} FROM ${RouteStopEntity.TABLE_NAME} INNER JOIN ${RouteInfoEntity.TABLE_NAME} ON ${RouteStopEntity.TABLE_NAME}.${RouteStopEntity.ROUTE} = ${RouteInfoEntity.TABLE_NAME}.${RouteInfoEntity.SUB_ROUTE} WHERE  ${RouteInfoEntity.TABLE_NAME}.${RouteInfoEntity.DOWN_TOWN} =:downTown AND  ${RouteInfoEntity.TABLE_NAME}.${RouteInfoEntity.SUB_ROUTE} =:route")
    fun searchStop(downTown: String, route: String): List<String>
}