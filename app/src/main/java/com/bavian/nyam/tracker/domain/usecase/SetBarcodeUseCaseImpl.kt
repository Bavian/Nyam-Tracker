package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.BarcodeInfo
import com.bavian.nyam.tracker.domain.repository.BarcodeRepository

class SetBarcodeUseCaseImpl(
    private val barcodeRepository: BarcodeRepository,
) : SetBarcodeUseCase {
    override suspend fun execute(barcode: BarcodeInfo) {
        barcodeRepository.setBarcode(barcode)
    }
}
