package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.domain.repository.ProductRepository

class AddProductUseCaseImpl(
    private val productRepository: ProductRepository,
) : AddProductUseCase {
    override suspend fun execute(product: Product) {
        productRepository.addProduct(product)
    }
}
