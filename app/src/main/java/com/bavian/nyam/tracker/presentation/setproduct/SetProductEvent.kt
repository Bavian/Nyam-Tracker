package com.bavian.nyam.tracker.presentation.setproduct

sealed interface SetProductEvent {
    data class ManufacturerChanged(
        val value: String,
    ) : SetProductEvent

    data class NameChanged(
        val value: String,
    ) : SetProductEvent

    data class CaloriesChanged(
        val value: String,
    ) : SetProductEvent

    data class ProteinsChanged(
        val value: String,
    ) : SetProductEvent

    data class FatChanged(
        val value: String,
    ) : SetProductEvent

    data class CarbohydratesChanged(
        val value: String,
    ) : SetProductEvent

    data object ConfirmClicked : SetProductEvent

    data object BackClicked : SetProductEvent
}
