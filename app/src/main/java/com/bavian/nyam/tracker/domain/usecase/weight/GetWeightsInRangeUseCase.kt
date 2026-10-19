package com.bavian.nyam.tracker.domain.usecase.weight

import com.bavian.nyam.tracker.domain.model.WeightsInfo

interface GetWeightsInRangeUseCase {
    suspend operator fun invoke(
        start: Long = 0L,
        end: Long = Long.MAX_VALUE,
    ): Result<WeightsInfo>
}
