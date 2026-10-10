package com.bavian.nyam.tracker.presentation.setproduct.mapper

import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModelState

interface SetProductUiStateMapper {
    fun mapToDomain(state: SetProductViewModelState): Product
}
