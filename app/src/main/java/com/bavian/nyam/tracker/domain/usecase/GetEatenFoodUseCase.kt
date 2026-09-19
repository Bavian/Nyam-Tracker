package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.EatenFood

interface GetEatenFoodUseCase {
    suspend fun execute(period: LongRange): List<EatenFood>
}
