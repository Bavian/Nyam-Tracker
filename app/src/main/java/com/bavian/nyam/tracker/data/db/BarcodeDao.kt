package com.bavian.nyam.tracker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bavian.nyam.tracker.data.model.BarcodeInfoEntity

@Dao
interface BarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarcode(barcode: BarcodeInfoEntity)

    @Query("SELECT * FROM barcodes WHERE number = :number")
    suspend fun getBarcodeByNumber(number: String): BarcodeInfoEntity?
}
