package com.bavian.nyam.tracker.presentation.setfood

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed class SetFoodViewModelEvent {
    data class ManufacturerChanged(
        val value: String,
    ) : SetFoodViewModelEvent()

    data class NameChanged(
        val value: String,
    ) : SetFoodViewModelEvent()

    data class WeightChanged(
        val value: String,
    ) : SetFoodViewModelEvent()

    data class CaloriesChanged(
        val value: String,
    ) : SetFoodViewModelEvent()

    data class ProteinsChanged(
        val value: String,
    ) : SetFoodViewModelEvent()

    data class FatChanged(
        val value: String,
    ) : SetFoodViewModelEvent()

    data class CarbohydratesChanged(
        val value: String,
    ) : SetFoodViewModelEvent()

    data class DateChanged(
        val value: LocalDate,
    ) : SetFoodViewModelEvent()

    data class TimeChanged(
        val value: LocalTime,
    ) : SetFoodViewModelEvent()

    data object SavePressed : SetFoodViewModelEvent()

    data object BackClicked : SetFoodViewModelEvent()
}
