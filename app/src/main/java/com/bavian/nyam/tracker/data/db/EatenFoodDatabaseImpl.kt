package com.bavian.nyam.tracker.data.db

import com.bavian.nyam.tracker.data.model.EatenFoodEntity

class EatenFoodDatabaseImpl(
    private val eatenFoodDao: EatenFoodDao,
) : EatenFoodDatabase {
    override fun insertEatenFood(eatenFood: EatenFoodEntity) {
        eatenFoodDao.insertEatenFood(eatenFood)
    }

    override fun getEatenFoodForPeriod(period: LongRange): List<EatenFoodEntity> =
        eatenFoodDao.getEatenFoodForPeriod(period.first, period.last)
}
