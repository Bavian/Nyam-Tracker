package com.bavian.nyam.tracker.domain.usecase.barcode

interface DeleteBarcodeUseCase {
    suspend operator fun invoke(number: String)
}
