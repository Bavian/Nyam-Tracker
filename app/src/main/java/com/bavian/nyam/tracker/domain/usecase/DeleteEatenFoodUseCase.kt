package com.bavian.nyam.tracker.domain.usecase

interface DeleteEatenFoodUseCase {
    suspend fun execute(id: String)
}
