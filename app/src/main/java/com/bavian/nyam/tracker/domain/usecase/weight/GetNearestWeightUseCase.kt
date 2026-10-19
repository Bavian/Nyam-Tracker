package com.bavian.nyam.tracker.domain.usecase.weight

import com.bavian.nyam.tracker.domain.model.WeightsInfo

interface GetNearestWeightUseCase {
    suspend operator fun invoke(timestamp: Long): Result<WeightsInfo.Item?>
}
