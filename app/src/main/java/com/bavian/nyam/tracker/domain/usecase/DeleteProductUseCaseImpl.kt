package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.repository.ProductRepository

class DeleteProductUseCaseImpl(
    private val productRepository: ProductRepository,
) : DeleteProductUseCase {
    override suspend fun execute(id: String) {
        productRepository.deleteProduct(id)
    }
}
