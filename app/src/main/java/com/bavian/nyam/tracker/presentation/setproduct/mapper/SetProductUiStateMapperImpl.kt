package com.bavian.nyam.tracker.presentation.setproduct.mapper

import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModelState
import com.bavian.nyam.tracker.utils.ClockUtil

class SetProductUiStateMapperImpl(
    private val clockUtil: ClockUtil,
) : SetProductUiStateMapper {
    override fun mapToDomain(state: SetProductViewModelState): Product =
        Product(
            id = state.id,
            manufacturer = state.manufacturer,
            name = state.name,
            calories = state.calories,
            proteins = state.proteins,
            fat = state.fat,
            carbohydrates = state.carbohydrates,
            price = state.price,
            timestamp = clockUtil.epoch,
        )
}
