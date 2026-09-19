package com.bavian.nyam.tracker.data.db

import com.bavian.nyam.tracker.data.model.EatenFoodEntity

interface EatenFoodDatabase {
    fun insertEatenFood(eatenFood: EatenFoodEntity)

    fun getEatenFoodForPeriod(period: LongRange): List<EatenFoodEntity>
}
