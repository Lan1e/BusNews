package com.example.busnews.database

import androidx.room.*

@Dao
interface DAO {
    @Insert
    fun insert(item: BusEntity): Long
    @Delete
    fun delete(item: BusEntity)
    @Update
    fun update(item: BusEntity)
//    @Query("")
//    fun query(): BusEntity
}