package com.bavian.nyam.tracker.presentation.scanner

interface BarcodeScanner {
    suspend fun startScan(): Result<ScannerState>

    data class ScannerState(
        val barcode: String,
    )
}
