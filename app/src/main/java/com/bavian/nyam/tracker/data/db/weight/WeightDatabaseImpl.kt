package com.bavian.nyam.tracker.data.db.weight

import com.bavian.nyam.tracker.data.model.WeightEntity

class WeightDatabaseImpl(
    private val weightDao: WeightDao,
) : WeightDatabase {
    override suspend fun insertWeight(weight: WeightEntity) {
        weightDao.insertWeight(weight)
    }

    override suspend fun deleteWeight(timestamp: Long) {
        weightDao.deleteWeight(timestamp)
    }

    override suspend fun getWeightsInRange(
        start: Long,
        end: Long,
    ): List<WeightEntity> = weightDao.getWeightsInRange(start, end)
}
