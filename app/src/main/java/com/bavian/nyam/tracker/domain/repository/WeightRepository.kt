package com.bavian.nyam.tracker.domain.repository

import com.bavian.nyam.tracker.domain.model.WeightsInfo

interface WeightRepository {
    suspend fun setWeight(weight: WeightsInfo.Item)

    suspend fun deleteWeight(timestamp: Long)

    suspend fun getWeightsInRange(
        start: Long,
        end: Long,
    ): WeightsInfo

    suspend fun getNearestWeight(timestamp: Long): WeightsInfo.Item?
}
