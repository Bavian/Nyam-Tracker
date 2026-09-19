package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.EatenFood

interface AddEatenFoodUseCase {
    suspend fun execute(eatenFood: EatenFood)
}
