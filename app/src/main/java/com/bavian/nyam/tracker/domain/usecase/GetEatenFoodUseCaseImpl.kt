package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.EatenFood
import com.bavian.nyam.tracker.domain.repository.EatenFoodRepository

class GetEatenFoodUseCaseImpl(
    private val eatenFoodRepository: EatenFoodRepository,
) : GetEatenFoodUseCase {
    override suspend fun execute(period: LongRange): List<EatenFood> = eatenFoodRepository.getEatenFoodForPeriod(period)
}
