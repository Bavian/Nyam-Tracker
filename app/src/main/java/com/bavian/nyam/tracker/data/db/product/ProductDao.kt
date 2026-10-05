package com.bavian.nyam.tracker.data.db.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bavian.nyam.tracker.data.model.ProductEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: String): ProductEntity?

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<ProductEntity>

    @Query("DELETE FROM products WHERE id = :id")
    fun deleteProduct(id: String)
}
