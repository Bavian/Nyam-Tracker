package com.bavian.nyam.tracker.data.repository

import com.bavian.nyam.tracker.data.db.ProductDatabase
import com.bavian.nyam.tracker.data.mapper.ProductMapper
import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val productDatabase: ProductDatabase,
    private val productMapper: ProductMapper,
) : ProductRepository {
    override suspend fun addProduct(product: Product) {
        withContext(Dispatchers.IO) {
            val entity = productMapper.mapToEntity(product)
            productDatabase.insertProduct(entity)
        }
    }

    override suspend fun getProductById(id: String): Product? =
        withContext(Dispatchers.IO) {
            val entity = productDatabase.getProductById(id)
            entity?.let { productMapper.mapToDomain(it) }
        }

    override suspend fun getAllProducts(): List<Product> =
        withContext(Dispatchers.IO) {
            productDatabase.getAllProducts().map { productMapper.mapToDomain(it) }
        }

    override suspend fun deleteProduct(id: String) {
        withContext(Dispatchers.IO) {
            productDatabase.deleteProduct(id)
        }
    }
}
