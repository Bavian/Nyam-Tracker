package com.bavian.nyam.tracker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bavian.nyam.tracker.data.model.EatenFoodEntity

@Dao
interface EatenFoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEatenFood(eatenFood: EatenFoodEntity)

    @Query("SELECT * FROM eaten_food WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp ASC")
    fun getEatenFoodForPeriod(
        start: Long,
        end: Long,
    ): List<EatenFoodEntity>
}
