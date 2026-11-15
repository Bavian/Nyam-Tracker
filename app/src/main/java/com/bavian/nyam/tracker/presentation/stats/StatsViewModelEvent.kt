package com.bavian.nyam.tracker.presentation.stats

sealed interface StatsViewModelEvent {
    data class DateRangePicked(
        val startDateDays: Long,
        val endDateDays: Long,
    ) : StatsViewModelEvent

    data object BackClicked : StatsViewModelEvent

    data object ScreenStarted : StatsViewModelEvent
}
