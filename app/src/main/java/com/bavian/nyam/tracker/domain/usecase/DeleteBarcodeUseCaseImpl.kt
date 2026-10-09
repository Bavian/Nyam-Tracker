package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.repository.BarcodeRepository

class DeleteBarcodeUseCaseImpl(
    private val barcodeRepository: BarcodeRepository,
) : DeleteBarcodeUseCase {
    override suspend fun invoke(number: String) {
        barcodeRepository.deleteBarcode(number)
    }
}
