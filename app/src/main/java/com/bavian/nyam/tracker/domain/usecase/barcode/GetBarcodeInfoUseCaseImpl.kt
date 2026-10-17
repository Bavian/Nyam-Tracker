package com.bavian.nyam.tracker.domain.usecase.barcode

import com.bavian.nyam.tracker.domain.model.BarcodeInfo
import com.bavian.nyam.tracker.domain.repository.BarcodeRepository

class GetBarcodeInfoUseCaseImpl(
    private val barcodeRepository: BarcodeRepository,
) : GetBarcodeInfoUseCase {
    override suspend fun execute(number: String): BarcodeInfo? = barcodeRepository.getBarcodeByNumber(number)
}
