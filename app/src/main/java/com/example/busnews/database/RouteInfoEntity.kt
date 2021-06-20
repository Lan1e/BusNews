package com.example.busnews.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.busnews.database.RouteInfoEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class RouteInfoEntity(
    var downTown: String = "",
    var mainRoute: String ,
    var subRoute:String
) {
    companion object {
        const val TABLE_NAME = "RouteInfoEntity"
        const val DOWN_TOWN = "downTown"
        const val MAIN_ROUTE = "mainRoute"
        const val SUB_ROUTE = "subRoute"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}