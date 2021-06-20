package com.example.busnews.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.busnews.database.RouteStopEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class RouteStopEntity(
    var route: String = "",
    var stop: String
) {
    companion object {
        const val TABLE_NAME = "RouteStopEntity"
        const val ROUTE = "route"
        const val STOP = "stop"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}