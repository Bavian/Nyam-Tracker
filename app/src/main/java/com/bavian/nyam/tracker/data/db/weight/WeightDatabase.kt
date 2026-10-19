package com.bavian.nyam.tracker.data.db.weight

import com.bavian.nyam.tracker.data.model.WeightEntity

interface WeightDatabase {
    suspend fun insertWeight(weight: WeightEntity)

    suspend fun deleteWeight(timestamp: Long)

    suspend fun getWeightsInRange(
        start: Long,
        end: Long,
    ): List<WeightEntity>
}
