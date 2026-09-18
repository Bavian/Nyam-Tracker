package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.BarcodeInfo

interface SetBarcodeUseCase {
    suspend fun execute(barcode: BarcodeInfo)
}
