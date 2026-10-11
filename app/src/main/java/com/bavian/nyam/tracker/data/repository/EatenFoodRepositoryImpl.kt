package com.bavian.nyam.tracker.data.repository

import com.bavian.nyam.tracker.data.db.EatenFoodDatabase
import com.bavian.nyam.tracker.data.mapper.EatenFoodMapper
import com.bavian.nyam.tracker.domain.model.EatenFood
import com.bavian.nyam.tracker.domain.repository.EatenFoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EatenFoodRepositoryImpl(
    private val eatenFoodDatabase: EatenFoodDatabase,
    private val eatenFoodMapper: EatenFoodMapper,
) : EatenFoodRepository {
    override suspend fun addEatenFood(eatenFood: EatenFood) {
        withContext(Dispatchers.IO) {
            val entity = eatenFoodMapper.mapToEntity(eatenFood)
            eatenFoodDatabase.insertEatenFood(entity)
        }
    }

    override suspend fun getEatenFoodForPeriod(period: LongRange): List<EatenFood> =
        withContext(Dispatchers.IO) {
            eatenFoodDatabase
                .getEatenFoodForPeriod(period)
                .map(eatenFoodMapper::mapToDomain)
        }

    override suspend fun getEatenFoodById(id: String): EatenFood? =
        withContext(Dispatchers.IO) {
            eatenFoodDatabase.getEatenFoodById(id)?.let(eatenFoodMapper::mapToDomain)
        }

    override suspend fun deleteEatenFood(id: String) {
        withContext(Dispatchers.IO) {
            eatenFoodDatabase.deleteEatenFood(id)
        }
    }
}
