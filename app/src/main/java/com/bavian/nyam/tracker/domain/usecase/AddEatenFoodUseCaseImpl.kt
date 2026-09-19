package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.EatenFood
import com.bavian.nyam.tracker.domain.repository.EatenFoodRepository

class AddEatenFoodUseCaseImpl(
    private val eatenFoodRepository: EatenFoodRepository,
) : AddEatenFoodUseCase {
    override suspend fun execute(eatenFood: EatenFood) {
        eatenFoodRepository.addEatenFood(eatenFood)
    }
}
