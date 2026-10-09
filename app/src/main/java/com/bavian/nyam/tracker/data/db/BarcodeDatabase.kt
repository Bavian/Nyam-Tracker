package com.bavian.nyam.tracker.data.db

import com.bavian.nyam.tracker.data.model.BarcodeInfoEntity

interface BarcodeDatabase {
    suspend fun insertBarcode(barcode: BarcodeInfoEntity)

    suspend fun getBarcodeByNumber(number: String): BarcodeInfoEntity?

    suspend fun getBarcodesByProductId(productId: String): List<BarcodeInfoEntity>
}
