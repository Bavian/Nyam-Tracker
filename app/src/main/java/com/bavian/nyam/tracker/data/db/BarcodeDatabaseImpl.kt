package com.bavian.nyam.tracker.data.db

import com.bavian.nyam.tracker.data.model.BarcodeInfoEntity

class BarcodeDatabaseImpl(
    private val barcodeDao: BarcodeDao,
) : BarcodeDatabase {
    override suspend fun insertBarcode(barcode: BarcodeInfoEntity) {
        barcodeDao.insertBarcode(barcode)
    }

    override suspend fun getBarcodeByNumber(number: String): BarcodeInfoEntity? =
        barcodeDao.getBarcodeByNumber(number)

    override suspend fun getBarcodesByProductId(productId: String): List<BarcodeInfoEntity> =
        barcodeDao.getBarcodesByProductId(productId)
}
