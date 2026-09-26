package com.bavian.nyam.tracker.presentation.setfood.mapper

import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.model.StringSource
import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.presentation.setfood.SetFoodViewModelState as ViewModelState
import com.bavian.nyam.tracker.presentation.setfood.compose.SetFoodScreen.State as ScreenState

internal class SetFoodScreenStateMapperImpl : SetFoodScreenStateMapper {
    override fun map(state: ViewModelState): ScreenState =
        ScreenState.Success(
            title =
                StringSource.Resource(
                    resId =
                        if (state.pastDay) {
                            R.string.set_food_title_past
                        } else {
                            R.string.set_food_title_future
                        },
                ),
            manufacturer = state.manufacturer.stringSource,
            name = state.name.stringSource,
            weight = state.weight.stringSource,
            kCalories = state.kCalories.stringSource,
            proteins = state.proteins.stringSource,
            fat = state.fat.stringSource,
            carbohydrates = state.carbohydrates.stringSource,
            date = state.date,
            time = state.time,
        )
}
