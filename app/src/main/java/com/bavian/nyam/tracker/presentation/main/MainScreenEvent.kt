package com.bavian.nyam.tracker.presentation.main

import kotlinx.datetime.LocalDate

sealed interface MainScreenEvent {
    data object StartScanTap : MainScreenEvent

    data object ProductsListTap : MainScreenEvent

    data class CalendarDatePicked(
        val date: LocalDate,
    ) : MainScreenEvent
}
