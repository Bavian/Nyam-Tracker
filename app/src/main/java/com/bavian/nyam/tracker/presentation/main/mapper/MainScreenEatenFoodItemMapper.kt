package com.bavian.nyam.tracker.presentation.main.mapper

import com.bavian.nyam.tracker.domain.model.EatenFood
import com.bavian.nyam.tracker.presentation.main.MainScreenViewModelState

interface MainScreenEatenFoodItemMapper {
    fun map(eatenFood: EatenFood): MainScreenViewModelState.Item
}
