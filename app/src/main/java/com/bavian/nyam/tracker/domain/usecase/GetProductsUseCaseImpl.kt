package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.infrastructure.StringDistanceCalculator
import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.domain.model.ProductSearchParams
import com.bavian.nyam.tracker.domain.repository.ProductRepository

class GetProductsUseCaseImpl(
    private val productRepository: ProductRepository,
    private val distanceCalculator: StringDistanceCalculator,
) : GetProductsUseCase {
    override suspend fun execute(searchParams: ProductSearchParams): List<Product> {
        val allProducts = productRepository.getAllProducts()
        val key = searchParams.key

        return if (key.isBlank()) {
            allProducts.sortedBy { it.name }
        } else {
            allProducts.sortedBy { product ->
                val nameDistance = distanceCalculator.calculate(product.name.lowercase(), key.lowercase())
                val manufacturerDistance = distanceCalculator.calculate(product.manufacturer.lowercase(), key.lowercase())
                minOf(nameDistance, manufacturerDistance)
            }
        }
    }
}
