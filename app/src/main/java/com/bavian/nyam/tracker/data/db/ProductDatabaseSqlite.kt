package com.bavian.nyam.tracker.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bavian.nyam.tracker.data.model.ProductEntity

class ProductDatabaseSqlite(
    context: Context,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ProductDatabase {
    companion object {
        private const val DATABASE_NAME = "products.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_PRODUCTS = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_MANUFACTURER = "manufacturer"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CALORIES = "calories"
        private const val COLUMN_PROTEINS = "proteins"
        private const val COLUMN_FAT = "fat"
        private const val COLUMN_CARBOHYDRATES = "carbohydrates"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            """
            CREATE TABLE ${TABLE_PRODUCTS} (
                ${COLUMN_ID} TEXT PRIMARY KEY,
                ${COLUMN_MANUFACTURER} TEXT,
                ${COLUMN_NAME} TEXT,
                ${COLUMN_CALORIES} INTEGER,
                ${COLUMN_PROTEINS} INTEGER,
                ${COLUMN_FAT} INTEGER,
                ${COLUMN_CARBOHYDRATES} INTEGER
            )
            """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int,
    ) {
        db.execSQL("DROP TABLE IF EXISTS ${TABLE_PRODUCTS}")
        onCreate(db)
    }

    override fun insertProduct(product: ProductEntity) {
        val db = writableDatabase
        val values =
            ContentValues().apply {
                put(COLUMN_ID, product.id)
                put(COLUMN_MANUFACTURER, product.manufacturer)
                put(COLUMN_NAME, product.name)
                put(COLUMN_CALORIES, product.calories)
                put(COLUMN_PROTEINS, product.proteins)
                put(COLUMN_FAT, product.fat)
                put(COLUMN_CARBOHYDRATES, product.carbohydrates)
            }
        db.insertWithOnConflict(TABLE_PRODUCTS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun getProductById(id: String): ProductEntity? {
        val db = readableDatabase
        val cursor =
            db.query(
                TABLE_PRODUCTS,
                null,
                "$COLUMN_ID = ?",
                arrayOf(id),
                null,
                null,
                null,
            )

        return cursor.use {
            if (it.moveToFirst()) {
                ProductEntity(
                    id = it.getString(it.getColumnIndexOrThrow(COLUMN_ID)),
                    manufacturer = it.getString(it.getColumnIndexOrThrow(COLUMN_MANUFACTURER)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    calories = it.getInt(it.getColumnIndexOrThrow(COLUMN_CALORIES)),
                    proteins = it.getInt(it.getColumnIndexOrThrow(COLUMN_PROTEINS)),
                    fat = it.getInt(it.getColumnIndexOrThrow(COLUMN_FAT)),
                    carbohydrates = it.getInt(it.getColumnIndexOrThrow(COLUMN_CARBOHYDRATES)),
                )
            } else {
                null
            }
        }
    }
}
