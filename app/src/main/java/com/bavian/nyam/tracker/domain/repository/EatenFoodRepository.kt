package com.bavian.nyam.tracker.domain.repository

import com.bavian.nyam.tracker.domain.model.EatenFood

interface EatenFoodRepository {
    suspend fun addEatenFood(eatenFood: EatenFood)

    suspend fun getEatenFoodForPeriod(period: LongRange): List<EatenFood>
}
