package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.Product

interface GetProductByIdUseCase {
    suspend fun execute(id: String): Product?
}
