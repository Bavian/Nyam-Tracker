package com.bavian.nyam.tracker.presentation.setproduct.mapper

import com.bavian.nyam.tracker.presentation.setproduct.SetProductScreen
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModelEvent

class SetProductEventMapperImpl : SetProductEventMapper {
    override fun map(event: SetProductScreen.Event): SetProductViewModelEvent =
        when (event) {
            is SetProductScreen.Event.ManufacturerChanged ->
                SetProductViewModelEvent.ManufacturerChanged(event.value)
            is SetProductScreen.Event.NameChanged ->
                SetProductViewModelEvent.NameChanged(event.value)
            is SetProductScreen.Event.CaloriesChanged ->
                SetProductViewModelEvent.CaloriesChanged(event.value)
            is SetProductScreen.Event.ProteinsChanged ->
                SetProductViewModelEvent.ProteinsChanged(event.value)
            is SetProductScreen.Event.FatChanged ->
                SetProductViewModelEvent.FatChanged(event.value)
            is SetProductScreen.Event.CarbohydratesChanged ->
                SetProductViewModelEvent.CarbohydratesChanged(event.value)
            is SetProductScreen.Event.ConfirmClicked ->
                SetProductViewModelEvent.ConfirmClicked
            is SetProductScreen.Event.BackClicked ->
                SetProductViewModelEvent.BackClicked
            is SetProductScreen.Event.BarcodesPlusClicked ->
                SetProductViewModelEvent.BarcodesPlusClicked
            is SetProductScreen.Event.BarcodeCrossClicked ->
                SetProductViewModelEvent.BarcodeCrossClicked(event.number)
            is SetProductScreen.Event.DeleteBarcodeConfirmed ->
                SetProductViewModelEvent.DeleteBarcodeConfirmed
            is SetProductScreen.Event.DeleteBarcodeCancelled ->
                SetProductViewModelEvent.DeleteBarcodeCancelled
            is SetProductScreen.Event.ScreenStarted ->
                SetProductViewModelEvent.ScreenStarted
        }
}
