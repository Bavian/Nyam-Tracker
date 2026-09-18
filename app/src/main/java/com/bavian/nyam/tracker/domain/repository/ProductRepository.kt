package com.bavian.nyam.tracker.domain.repository

import com.bavian.nyam.tracker.domain.model.Product

interface ProductRepository {
    suspend fun addProduct(product: Product)

    suspend fun getProductById(id: String): Product?

    suspend fun getAllProducts(): List<Product>
}
