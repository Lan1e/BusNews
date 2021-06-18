package com.example.busnews.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(DownTownRouteEntity::class), (RouteStopEntity::class)], version = 1)
abstract class BusDatabase : RoomDatabase() {
    companion object {
        @Volatile private var instance: BusDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            BusDatabase::class.java, "item-list.db").build()
    }

    abstract fun getRoomDao(): DAO
}