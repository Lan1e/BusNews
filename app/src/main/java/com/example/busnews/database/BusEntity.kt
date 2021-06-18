package com.example.busnews.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.busnews.database.BusEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class BusEntity(
    var name: String = ""
) {
    companion object {
        const val TABLE_NAME = "room_entity"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}