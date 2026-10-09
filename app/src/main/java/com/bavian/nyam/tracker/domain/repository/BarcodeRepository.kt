package com.bavian.nyam.tracker.domain.repository

import com.bavian.nyam.tracker.domain.model.BarcodeInfo
import com.bavian.nyam.tracker.domain.model.ProductBarcodes

interface BarcodeRepository {
    suspend fun setBarcode(barcode: BarcodeInfo)

    suspend fun getBarcodeByNumber(number: String): BarcodeInfo?

    suspend fun getBarcodesByProductId(productId: String): ProductBarcodes

    suspend fun deleteBarcode(number: String)
}
