package com.bavian.nyam.tracker.domain.usecase.barcode

import com.bavian.nyam.tracker.domain.model.BarcodeInfo

interface SetBarcodeUseCase {
    suspend fun execute(barcode: BarcodeInfo)
}
