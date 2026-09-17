package com.bavian.nyam.tracker.presentation.scanner

import android.content.Context

interface BarcodeScanner {
    suspend fun startScan(context: Context): Result<ScannerState>

    data class ScannerState(
        val barcode: String,
    )
}
