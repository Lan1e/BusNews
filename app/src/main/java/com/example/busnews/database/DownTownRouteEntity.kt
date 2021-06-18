package com.example.busnews.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.busnews.database.DownTownRouteEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class DownTownRouteEntity(
    var downTown: String = "",
    var route: String = ""
) {
    companion object {
        const val TABLE_NAME = "DownTownRouteEntity"
        const val DOWN_TOWN = "downTown"
        const val ROUTE = "route"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}