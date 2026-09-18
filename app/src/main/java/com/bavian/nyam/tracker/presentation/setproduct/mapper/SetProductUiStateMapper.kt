package com.bavian.nyam.tracker.presentation.setproduct.mapper

import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.presentation.setproduct.SetProductUiState

interface SetProductUiStateMapper {
    fun mapToDomain(state: SetProductUiState): Product
}
