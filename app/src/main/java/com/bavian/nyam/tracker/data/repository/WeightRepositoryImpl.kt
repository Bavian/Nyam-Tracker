package com.bavian.nyam.tracker.data.repository

import com.bavian.nyam.tracker.base.mapAsImmutableList
import com.bavian.nyam.tracker.data.db.weight.WeightDatabase
import com.bavian.nyam.tracker.data.mapper.WeightMapper
import com.bavian.nyam.tracker.domain.model.WeightsInfo
import com.bavian.nyam.tracker.domain.repository.WeightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

class WeightRepositoryImpl(
    private val weightDatabase: WeightDatabase,
    private val weightMapper: WeightMapper,
) : WeightRepository {
    override suspend fun setWeight(weight: WeightsInfo.Item) =
        withContext(Dispatchers.IO) {
            val entity = weightMapper.mapToEntity(weight)
            weightDatabase.insertWeight(entity)
        }

    override suspend fun deleteWeight(timestamp: Long) =
        withContext(Dispatchers.IO) {
            weightDatabase.deleteWeight(timestamp)
        }

    override suspend fun getWeightsInRange(
        start: Long,
        end: Long,
    ): WeightsInfo =
        withContext(Dispatchers.IO) {
            WeightsInfo(
                items =
                    weightDatabase
                        .getWeightsInRange(start, end)
                        .mapAsImmutableList(weightMapper::mapToDomain),
            )
        }

    override suspend fun getNearestWeight(timestamp: Long): WeightsInfo.Item? =
        withContext(Dispatchers.IO) {
            val weights = weightDatabase.getWeightsInRange(0, Long.MAX_VALUE)
            if (weights.isEmpty()) return@withContext null

            val nearest = weights.minByOrNull { abs(it.timestamp - timestamp) }
            nearest?.let { weightMapper.mapToDomain(it) }
        }
}
