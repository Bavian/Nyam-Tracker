package com.bavian.nyam.tracker.presentation.scanner

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface BarcodeScanner {
    sealed interface ScannerState {
        data object Idle : ScannerState
        data object Loading : ScannerState
        data class Success(val barcodeValue: String) : ScannerState
        data class Error(val errorMessage: String) : ScannerState
    }

    fun startScan(context: Context): Flow<ScannerState>
}
