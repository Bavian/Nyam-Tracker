package com.bavian.nyam.tracker

data class UiState(
    val scanState: BarcodeScanner.ScannerState = BarcodeScanner.ScannerState.Idle,
    val resultState: ResultState = ResultState.Initial
) {
    sealed interface ResultState {
        data object Initial : ResultState
        data object Loading : ResultState
        data class Success(val outputText: String) : ResultState
        data class Error(val errorMessage: String) : ResultState
    }
}
