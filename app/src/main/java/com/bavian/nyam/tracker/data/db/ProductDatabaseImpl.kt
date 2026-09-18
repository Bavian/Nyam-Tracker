package com.bavian.nyam.tracker.data.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.bavian.nyam.tracker.data.model.ProductEntity

class ProductDatabaseImpl(
    private val dbHelper: AppDatabaseHelper,
) : ProductDatabase {
    override fun insertProduct(product: ProductEntity) {
        val db = dbHelper.writableDatabase
        val values =
            ContentValues().apply {
                put(AppDatabaseHelper.COLUMN_PRODUCT_ID, product.id)
                put(AppDatabaseHelper.COLUMN_MANUFACTURER, product.manufacturer)
                put(AppDatabaseHelper.COLUMN_NAME, product.name)
                put(AppDatabaseHelper.COLUMN_CALORIES, product.calories)
                put(AppDatabaseHelper.COLUMN_PROTEINS, product.proteins)
                put(AppDatabaseHelper.COLUMN_FAT, product.fat)
                put(AppDatabaseHelper.COLUMN_CARBOHYDRATES, product.carbohydrates)
            }
        db.insertWithOnConflict(AppDatabaseHelper.TABLE_PRODUCTS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun getProductById(id: String): ProductEntity? {
        val db = dbHelper.readableDatabase
        val cursor =
            db.query(
                AppDatabaseHelper.TABLE_PRODUCTS,
                null,
                "${AppDatabaseHelper.COLUMN_PRODUCT_ID} = ?",
                arrayOf(id),
                null,
                null,
                null,
            )

        return cursor.use {
            if (it.moveToFirst()) {
                ProductEntity(
                    id = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_PRODUCT_ID)),
                    manufacturer = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_MANUFACTURER)),
                    name = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_NAME)),
                    calories = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_CALORIES)),
                    proteins = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_PROTEINS)),
                    fat = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_FAT)),
                    carbohydrates = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_CARBOHYDRATES)),
                )
            } else {
                null
            }
        }
    }
}
