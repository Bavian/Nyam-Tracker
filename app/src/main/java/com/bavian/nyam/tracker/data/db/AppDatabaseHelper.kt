package com.bavian.nyam.tracker.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(
    context: Context,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "nyam_tracker.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_PRODUCTS = "products"
        const val COLUMN_PRODUCT_ID = "id"
        const val COLUMN_MANUFACTURER = "manufacturer"
        const val COLUMN_NAME = "name"
        const val COLUMN_CALORIES = "calories"
        const val COLUMN_PROTEINS = "proteins"
        const val COLUMN_FAT = "fat"
        const val COLUMN_CARBOHYDRATES = "carbohydrates"

        const val TABLE_BARCODES = "barcodes"
        const val COLUMN_BARCODE_NUMBER = "number"
        const val COLUMN_PRODUCT_IDS = "product_ids"

        const val TABLE_EATEN_FOOD = "eaten_food"
        const val COLUMN_EATEN_FOOD_ID = "id"
        const val COLUMN_EATEN_FOOD_MANUFACTURER = "manufacturer"
        const val COLUMN_EATEN_FOOD_NAME = "name"
        const val COLUMN_EATEN_FOOD_KCALORIES = "kcalories"
        const val COLUMN_EATEN_FOOD_PROTEINS = "proteins"
        const val COLUMN_EATEN_FOOD_FAT = "fat"
        const val COLUMN_EATEN_FOOD_CARBOHYDRATES = "carbohydrates"
        const val COLUMN_EATEN_FOOD_TIMESTAMP = "timestamp"

        private val CREATE_EATEN_FOOD_TABLE =
            """
            CREATE TABLE $TABLE_EATEN_FOOD (
                $COLUMN_EATEN_FOOD_ID TEXT PRIMARY KEY,
                $COLUMN_EATEN_FOOD_MANUFACTURER TEXT,
                $COLUMN_EATEN_FOOD_NAME TEXT,
                $COLUMN_EATEN_FOOD_KCALORIES INTEGER,
                $COLUMN_EATEN_FOOD_PROTEINS INTEGER,
                $COLUMN_EATEN_FOOD_FAT INTEGER,
                $COLUMN_EATEN_FOOD_CARBOHYDRATES INTEGER,
                $COLUMN_EATEN_FOOD_TIMESTAMP INTEGER
            )
            """.trimIndent()
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createProductsTable =
            """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_PRODUCT_ID TEXT PRIMARY KEY,
                $COLUMN_MANUFACTURER TEXT,
                $COLUMN_NAME TEXT,
                $COLUMN_CALORIES INTEGER,
                $COLUMN_PROTEINS INTEGER,
                $COLUMN_FAT INTEGER,
                $COLUMN_CARBOHYDRATES INTEGER
            )
            """.trimIndent()
        db.execSQL(createProductsTable)

        val createBarcodesTable =
            """
            CREATE TABLE $TABLE_BARCODES (
                $COLUMN_BARCODE_NUMBER TEXT PRIMARY KEY,
                $COLUMN_PRODUCT_IDS TEXT
            )
            """.trimIndent()
        db.execSQL(createBarcodesTable)

        db.execSQL(CREATE_EATEN_FOOD_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int,
    ) {
        if (oldVersion < 2) {
            db.execSQL(CREATE_EATEN_FOOD_TABLE)
        }
    }
}
