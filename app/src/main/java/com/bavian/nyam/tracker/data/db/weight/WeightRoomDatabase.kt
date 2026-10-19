package com.bavian.nyam.tracker.data.db.weight

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bavian.nyam.tracker.data.model.WeightEntity

@Database(entities = [WeightEntity::class], version = 1)
abstract class WeightRoomDatabase : RoomDatabase() {
    abstract fun weightDao(): WeightDao
}
