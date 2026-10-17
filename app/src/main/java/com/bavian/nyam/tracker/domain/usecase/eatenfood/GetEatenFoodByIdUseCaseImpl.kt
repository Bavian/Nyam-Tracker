package com.bavian.nyam.tracker.domain.usecase.eatenfood

import com.bavian.nyam.tracker.domain.model.EatenFood
import com.bavian.nyam.tracker.domain.repository.EatenFoodRepository

class GetEatenFoodByIdUseCaseImpl(
    private val eatenFoodRepository: EatenFoodRepository,
) : GetEatenFoodByIdUseCase {
    override suspend fun execute(id: String): EatenFood? = eatenFoodRepository.getEatenFoodById(id)
}
