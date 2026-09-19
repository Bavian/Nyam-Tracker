package com.bavian.nyam.tracker.domain.usecase

interface DeleteProductUseCase {
    suspend fun execute(id: String)
}
