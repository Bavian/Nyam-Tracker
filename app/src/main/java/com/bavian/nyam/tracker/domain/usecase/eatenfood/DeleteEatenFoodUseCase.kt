package com.bavian.nyam.tracker.domain.usecase.eatenfood

interface DeleteEatenFoodUseCase {
    suspend fun execute(id: String)
}
