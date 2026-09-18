package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.BarcodeInfo

interface GetBarcodeInfoUseCase {
    suspend fun execute(number: String): BarcodeInfo?
}
