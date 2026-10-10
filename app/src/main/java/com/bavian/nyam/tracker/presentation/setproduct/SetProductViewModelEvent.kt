package com.bavian.nyam.tracker.presentation.setproduct

sealed interface SetProductViewModelEvent {
    data class ManufacturerChanged(
        val value: String,
    ) : SetProductViewModelEvent

    data class NameChanged(
        val value: String,
    ) : SetProductViewModelEvent

    data class CaloriesChanged(
        val value: String,
    ) : SetProductViewModelEvent

    data class ProteinsChanged(
        val value: String,
    ) : SetProductViewModelEvent

    data class FatChanged(
        val value: String,
    ) : SetProductViewModelEvent

    data class CarbohydratesChanged(
        val value: String,
    ) : SetProductViewModelEvent

    data object ConfirmClicked : SetProductViewModelEvent

    data object BackClicked : SetProductViewModelEvent

    data object BarcodesPlusClicked : SetProductViewModelEvent

    data class BarcodeCrossClicked(
        val number: String,
    ) : SetProductViewModelEvent

    data object DeleteBarcodeConfirmed : SetProductViewModelEvent

    data object DeleteBarcodeCancelled : SetProductViewModelEvent

    data object ScreenStarted : SetProductViewModelEvent
}
