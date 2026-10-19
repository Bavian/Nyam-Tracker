package com.bavian.nyam.tracker.domain.usecase.weight

interface DeleteWeightUseCase {
    suspend operator fun invoke(timestamp: Long): Result<Unit>
}
