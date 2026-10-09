package com.bavian.nyam.tracker.domain.usecase

interface DeleteBarcodeUseCase {
    suspend operator fun invoke(number: String)
}
