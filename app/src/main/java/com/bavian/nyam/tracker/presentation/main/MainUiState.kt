package com.bavian.nyam.tracker.presentation.main

import com.bavian.nyam.tracker.presentation.scanner.BarcodeScanner

data class MainUiState(
    val scanState: BarcodeScanner.ScannerState = BarcodeScanner.ScannerState.Idle,
    val resultState: ResultState = ResultState.Initial,
) {
    sealed interface ResultState {
        data object Initial : ResultState

        data object Loading : ResultState

        data class Success(
            val outputText: String,
        ) : ResultState

        data class Error(
            val errorMessage: String,
        ) : ResultState
    }
}
