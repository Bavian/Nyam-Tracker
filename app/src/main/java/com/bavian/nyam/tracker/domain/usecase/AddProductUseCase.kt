package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.Product

interface AddProductUseCase {
    suspend fun execute(product: Product)
}
