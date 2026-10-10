package com.bavian.nyam.tracker.presentation.setproduct.mapper

import com.bavian.nyam.tracker.base.mapAsImmutableList
import com.bavian.nyam.tracker.presentation.setproduct.SetProductScreen
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModelState
import com.bavian.nyam.tracker.ui.model.stringSource

class SetProductStateMapperImpl : SetProductStateMapper {
    override fun map(state: SetProductViewModelState): SetProductScreen.State =
        SetProductScreen.State.Success(
            manufacturer = state.manufacturer.stringSource,
            name = state.name.stringSource,
            calories = state.calories.stringSource,
            proteins = state.proteins.stringSource,
            fat = state.fat.stringSource,
            carbohydrates = state.carbohydrates.stringSource,
            barcodes = state.barcodes.mapAsImmutableList { it.stringSource },
            barcodeToDelete = state.barcodeToDelete.stringSource,
        )
}
