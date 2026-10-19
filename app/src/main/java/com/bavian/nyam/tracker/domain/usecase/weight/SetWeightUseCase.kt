package com.bavian.nyam.tracker.domain.usecase.weight

import com.bavian.nyam.tracker.domain.model.WeightsInfo

interface SetWeightUseCase {
    suspend operator fun invoke(weight: WeightsInfo.Item): Result<Unit>
}
