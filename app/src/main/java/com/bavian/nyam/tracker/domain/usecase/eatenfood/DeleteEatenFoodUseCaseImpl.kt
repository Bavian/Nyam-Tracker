package com.bavian.nyam.tracker.domain.usecase.eatenfood

import com.bavian.nyam.tracker.domain.repository.EatenFoodRepository

class DeleteEatenFoodUseCaseImpl(
    private val eatenFoodRepository: EatenFoodRepository,
) : DeleteEatenFoodUseCase {
    override suspend fun execute(id: String) {
        eatenFoodRepository.deleteEatenFood(id)
    }
}
