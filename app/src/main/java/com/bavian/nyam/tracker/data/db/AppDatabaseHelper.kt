package com.bavian.nyam.tracker.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(
    context: Context,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "nyam_tracker.db"
        private const val DATABASE_VERSION = 1

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
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int,
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BARCODES")
        onCreate(db)
    }
}
