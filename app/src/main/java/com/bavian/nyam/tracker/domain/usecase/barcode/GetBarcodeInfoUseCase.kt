package com.bavian.nyam.tracker.domain.usecase.barcode

import com.bavian.nyam.tracker.domain.model.BarcodeInfo

interface GetBarcodeInfoUseCase {
    suspend fun execute(number: String): BarcodeInfo?
}
