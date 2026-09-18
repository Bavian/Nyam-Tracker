package com.bavian.nyam.tracker.data.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.bavian.nyam.tracker.data.model.BarcodeInfoEntity

class BarcodeDatabaseImpl(
    private val dbHelper: AppDatabaseHelper,
) : BarcodeDatabase {
    override fun insertBarcode(barcode: BarcodeInfoEntity) {
        val db = dbHelper.writableDatabase
        val values =
            ContentValues().apply {
                put(AppDatabaseHelper.COLUMN_BARCODE_NUMBER, barcode.number)
                put(AppDatabaseHelper.COLUMN_PRODUCT_IDS, barcode.productIds)
            }
        db.insertWithOnConflict(AppDatabaseHelper.TABLE_BARCODES, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun getBarcodeByNumber(number: String): BarcodeInfoEntity? {
        val db = dbHelper.readableDatabase
        val cursor =
            db.query(
                AppDatabaseHelper.TABLE_BARCODES,
                null,
                "${AppDatabaseHelper.COLUMN_BARCODE_NUMBER} = ?",
                arrayOf(number),
                null,
                null,
                null,
            )

        return cursor.use {
            if (it.moveToFirst()) {
                BarcodeInfoEntity(
                    number = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_BARCODE_NUMBER)),
                    productIds = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_PRODUCT_IDS)),
                )
            } else {
                null
            }
        }
    }
}
