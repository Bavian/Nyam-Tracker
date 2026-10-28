package com.bavian.nyam.tracker.presentation.setweight

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface SetWeightViewModelEvent {
    data class WeightChanged(
        val value: String,
    ) : SetWeightViewModelEvent

    data class DateChanged(
        val value: LocalDate,
    ) : SetWeightViewModelEvent

    data class TimeChanged(
        val value: LocalTime,
    ) : SetWeightViewModelEvent

    data object SavePressed : SetWeightViewModelEvent

    data object BackClicked : SetWeightViewModelEvent

    data object DeleteClicked : SetWeightViewModelEvent

    data object DeleteConfirmed : SetWeightViewModelEvent

    data object DeleteCancelled : SetWeightViewModelEvent
}
