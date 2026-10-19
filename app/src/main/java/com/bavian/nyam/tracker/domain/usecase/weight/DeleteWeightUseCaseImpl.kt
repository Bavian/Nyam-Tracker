package com.bavian.nyam.tracker.domain.usecase.weight

import com.bavian.nyam.tracker.domain.repository.WeightRepository

class DeleteWeightUseCaseImpl(
    private val weightRepository: WeightRepository,
) : DeleteWeightUseCase {
    override suspend operator fun invoke(timestamp: Long) =
        runCatching {
            weightRepository.deleteWeight(timestamp)
        }
}
