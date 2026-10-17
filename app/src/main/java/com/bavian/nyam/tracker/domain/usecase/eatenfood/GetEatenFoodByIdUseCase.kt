package com.bavian.nyam.tracker.domain.usecase.eatenfood

import com.bavian.nyam.tracker.domain.model.EatenFood

interface GetEatenFoodByIdUseCase {
    suspend fun execute(id: String): EatenFood?
}
