package com.bavian.nyam.tracker.domain.usecase.weight

import com.bavian.nyam.tracker.domain.model.WeightsInfo
import com.bavian.nyam.tracker.domain.repository.WeightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetNearestWeightUseCaseImpl(
    private val weightRepository: WeightRepository,
) : GetNearestWeightUseCase {
    override suspend operator fun invoke(timestamp: Long): Result<WeightsInfo.Item?> =
        withContext(Dispatchers.IO) {
            try {
                val result = weightRepository.getNearestWeight(timestamp)
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
