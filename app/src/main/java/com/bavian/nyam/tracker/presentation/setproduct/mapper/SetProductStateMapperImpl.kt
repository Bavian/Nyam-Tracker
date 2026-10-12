package com.bavian.nyam.tracker.presentation.setproduct.mapper

import com.bavian.nyam.tracker.base.mapAsImmutableList
import com.bavian.nyam.tracker.presentation.setproduct.SetProductScreen
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModelState
import com.bavian.nyam.tracker.ui.model.stringSource

class SetProductStateMapperImpl : SetProductStateMapper {
    override fun map(state: SetProductViewModelState): SetProductScreen.State {
        if (state.productLoading || state.barcodesLoading) {
            return SetProductScreen.State.Loading(
                barcodeToDelete = state.barcodeToDelete.stringSource,
            )
        }

        return SetProductScreen.State.Success(
            manufacturer = state.manufacturer.stringSource,
            name = state.name.stringSource,
            calories = state.calories,
            proteins = state.proteins,
            fat = state.fat,
            carbohydrates = state.carbohydrates,
            barcodes = state.barcodes.mapAsImmutableList { it.stringSource },
            barcodeToDelete = state.barcodeToDelete.stringSource,
        )
    }
}
