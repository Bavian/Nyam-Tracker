package com.bavian.nyam.tracker.domain.repository

import com.bavian.nyam.tracker.domain.model.BarcodeInfo

interface BarcodeRepository {
    suspend fun setBarcode(barcode: BarcodeInfo)

    suspend fun getBarcodeByNumber(number: String): BarcodeInfo?
}
