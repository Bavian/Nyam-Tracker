package com.bavian.nyam.tracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bavian.nyam.tracker.data.model.EatenFoodEntity

@Database(entities = [EatenFoodEntity::class], version = 1)
abstract class EatenFoodRoomDatabase : RoomDatabase() {
    abstract fun eatenFoodDao(): EatenFoodDao
}
