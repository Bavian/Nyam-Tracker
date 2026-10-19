package com.bavian.nyam.tracker.domain.usecase.weight

import com.bavian.nyam.tracker.domain.model.WeightsInfo
import com.bavian.nyam.tracker.domain.repository.WeightRepository

class GetWeightsInRangeUseCaseImpl(
    private val weightRepository: WeightRepository,
) : GetWeightsInRangeUseCase {
    override suspend operator fun invoke(
        start: Long,
        end: Long,
    ): Result<WeightsInfo> =
        runCatching {
            weightRepository.getWeightsInRange(start, end)
        }
}
