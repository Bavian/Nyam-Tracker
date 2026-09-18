package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.domain.repository.ProductRepository

class GetProductByIdUseCaseImpl(
    private val productRepository: ProductRepository,
) : GetProductByIdUseCase {
    override suspend fun execute(id: String): Product? = productRepository.getProductById(id)
}
