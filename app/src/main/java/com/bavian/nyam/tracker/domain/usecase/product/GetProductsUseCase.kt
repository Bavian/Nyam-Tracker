package com.bavian.nyam.tracker.domain.usecase.product

import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.domain.model.ProductSearchParams

interface GetProductsUseCase {
    suspend fun execute(searchParams: ProductSearchParams): List<Product>
}
