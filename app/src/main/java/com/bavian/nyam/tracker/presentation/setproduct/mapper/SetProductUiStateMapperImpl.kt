package com.bavian.nyam.tracker.presentation.setproduct.mapper

import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModelState

class SetProductUiStateMapperImpl : SetProductUiStateMapper {
    override fun mapToDomain(state: SetProductViewModelState): Product =
        Product(
            id = state.id,
            manufacturer = state.manufacturer,
            name = state.name,
            calories = state.calories.toFloatOrNull() ?: 0f,
            proteins = state.proteins.toFloatOrNull() ?: 0f,
            fat = state.fat.toFloatOrNull() ?: 0f,
            carbohydrates = state.carbohydrates.toFloatOrNull() ?: 0f,
        )
}
