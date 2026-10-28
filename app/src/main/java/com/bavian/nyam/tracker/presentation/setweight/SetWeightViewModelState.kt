package com.bavian.nyam.tracker.presentation.setweight

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class SetWeightViewModelState(
    val weight: String = "",
    val date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val time: LocalTime =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .time,
    val canDelete: Boolean = false,
    val deleteConfirmation: Boolean = false,
    val loading: Boolean = false,
    val recordTimestamp: Long? = null,
)
