package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.ProductBarcodes
import com.bavian.nyam.tracker.domain.repository.BarcodeRepository

class GetBarcodesByProductIdUseCaseImpl(
    private val barcodeRepository: BarcodeRepository,
) : GetBarcodesByProductIdUseCase {
    override suspend fun execute(productId: String): ProductBarcodes = barcodeRepository.getBarcodesByProductId(productId)
}
