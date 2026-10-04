package com.bavian.nyam.tracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bavian.nyam.tracker.data.model.BarcodeInfoEntity

@Database(entities = [BarcodeInfoEntity::class], version = 1)
abstract class BarcodeRoomDatabase : RoomDatabase() {
    abstract fun barcodeDao(): BarcodeDao
}
