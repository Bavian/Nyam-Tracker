package com.bavian.nyam.tracker.presentation.main

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class MainUiState(
    val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
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
