package com.bavian.nyam.tracker.data.db

import com.bavian.nyam.tracker.data.model.BarcodeInfoEntity

interface BarcodeDatabase {
    fun insertBarcode(barcode: BarcodeInfoEntity)

    fun getBarcodeByNumber(number: String): BarcodeInfoEntity?
}
