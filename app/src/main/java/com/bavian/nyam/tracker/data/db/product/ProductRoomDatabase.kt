package com.bavian.nyam.tracker.data.db.product

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bavian.nyam.tracker.data.model.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductRoomDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
