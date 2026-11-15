package com.bavian.nyam.tracker.presentation.stats

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class StatsViewModelState(
    val startDateDays: Long = defaultStartDateDays(),
    val endDateDays: Long = defaultEndDateDays(),
    val dailyStats: ImmutableList<StatsScreen.DailyStat> = persistentListOf(),
    val loading: Boolean = false,
)

private fun defaultStartDateDays(): Long {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val mondayOffset = today.dayOfWeek.ordinal
    return (today - DatePeriod(days = mondayOffset)).toEpochDays()
}

private fun defaultEndDateDays(): Long {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val mondayOffset = today.dayOfWeek.ordinal
    val startOfWeek = today - DatePeriod(days = mondayOffset)
    return (startOfWeek + DatePeriod(days = 6)).toEpochDays()
}
