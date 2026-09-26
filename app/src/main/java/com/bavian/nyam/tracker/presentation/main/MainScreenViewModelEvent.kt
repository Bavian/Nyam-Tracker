package com.bavian.nyam.tracker.presentation.main

import kotlinx.datetime.LocalDate

sealed interface MainScreenViewModelEvent {
    data object StartScanTap : MainScreenViewModelEvent

    data object ProductsListTap : MainScreenViewModelEvent

    data class CalendarDatePicked(
        val date: LocalDate,
    ) : MainScreenViewModelEvent

    data object ScreenViewModelStarted : MainScreenViewModelEvent

    data class EatenFoodTap(
        val foodId: String,
    ) : MainScreenViewModelEvent
}
