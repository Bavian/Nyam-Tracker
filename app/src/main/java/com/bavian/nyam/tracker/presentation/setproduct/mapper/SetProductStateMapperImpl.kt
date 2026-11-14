package com.bavian.nyam.tracker.presentation.setproduct.mapper

import com.bavian.nyam.tracker.presentation.setproduct.SetProductScreen
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModelState
import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.utils.buildPersistentList

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
            price = state.price,
            barcodes =
                buildPersistentList {
                    state.barcodes.forEach { add(it.stringSource) }
                    if (!state.initialBarcode.isNullOrBlank() && (state.initialBarcode !in state.barcodes)) {
                        add(state.initialBarcode.stringSource)
                    }
                },
            barcodeToDelete = state.barcodeToDelete.stringSource,
        )
    }
}
