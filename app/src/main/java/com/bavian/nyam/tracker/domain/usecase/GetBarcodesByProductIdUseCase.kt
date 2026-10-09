package com.bavian.nyam.tracker.domain.usecase

import com.bavian.nyam.tracker.domain.model.ProductBarcodes

interface GetBarcodesByProductIdUseCase {
    suspend fun execute(productId: String): ProductBarcodes
}
