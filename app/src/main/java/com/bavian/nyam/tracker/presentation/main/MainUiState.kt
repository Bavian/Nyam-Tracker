package com.bavian.nyam.tracker.presentation.main

data class MainUiState(
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
