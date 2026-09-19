package com.bavian.nyam.tracker.data.db

import com.bavian.nyam.tracker.data.model.ProductEntity

interface ProductDatabase {
    fun insertProduct(product: ProductEntity)

    fun getProductById(id: String): ProductEntity?

    fun getAllProducts(): List<ProductEntity>

    fun deleteProduct(id: String)
}
