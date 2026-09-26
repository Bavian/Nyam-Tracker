package com.bavian.nyam.tracker.presentation.main.mapper

import com.bavian.nyam.tracker.domain.model.EatenFood
import com.bavian.nyam.tracker.presentation.main.MainScreenViewModelState

class MainScreenEatenFoodItemMapperImpl : MainScreenEatenFoodItemMapper {
    override fun map(eatenFood: EatenFood): MainScreenViewModelState.Item =
        MainScreenViewModelState.Item(
            id = eatenFood.id,
            name = eatenFood.name,
            manufacturer = eatenFood.manufacturer,
            weight = eatenFood.weight,
            calories = eatenFood.kCalories,
            proteins = eatenFood.proteins,
            fat = eatenFood.fat,
            carbohydrates = eatenFood.carbohydrates,
            timestamp = eatenFood.timestamp,
        )
}
