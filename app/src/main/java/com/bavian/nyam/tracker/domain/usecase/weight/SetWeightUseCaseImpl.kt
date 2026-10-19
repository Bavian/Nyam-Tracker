package com.bavian.nyam.tracker.domain.usecase.weight

import com.bavian.nyam.tracker.domain.model.WeightsInfo
import com.bavian.nyam.tracker.domain.repository.WeightRepository

class SetWeightUseCaseImpl(
    private val weightRepository: WeightRepository,
) : SetWeightUseCase {
    override suspend operator fun invoke(weight: WeightsInfo.Item) =
        runCatching {
            weightRepository.setWeight(weight)
        }
}
