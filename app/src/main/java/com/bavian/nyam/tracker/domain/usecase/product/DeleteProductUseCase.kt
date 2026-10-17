package com.bavian.nyam.tracker.domain.usecase.product

interface DeleteProductUseCase {
    suspend fun execute(id: String)
}
