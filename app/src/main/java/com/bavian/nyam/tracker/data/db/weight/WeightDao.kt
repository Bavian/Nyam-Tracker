package com.bavian.nyam.tracker.data.db.weight

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bavian.nyam.tracker.data.model.WeightEntity

@Dao
interface WeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weight: WeightEntity)

    @Query("DELETE FROM weights WHERE timestamp = :timestamp")
    suspend fun deleteWeight(timestamp: Long)

    @Query("SELECT * FROM weights WHERE timestamp BETWEEN :start AND :end")
    suspend fun getWeightsInRange(
        start: Long,
        end: Long,
    ): List<WeightEntity>

    @Query("SELECT * FROM weights")
    suspend fun getWeights(): List<WeightEntity>
}
