package com.bavian.nyam.tracker.data.db.product

import com.bavian.nyam.tracker.data.model.ProductEntity

class ProductDatabaseImpl(
    private val productDao: ProductDao,
) : ProductDatabase {
    override fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    override fun getProductById(id: String): ProductEntity? = productDao.getProductById(id)

    override fun getAllProducts(): List<ProductEntity> = productDao.getAllProducts()

    override fun deleteProduct(id: String) {
        productDao.deleteProduct(id)
    }
}
