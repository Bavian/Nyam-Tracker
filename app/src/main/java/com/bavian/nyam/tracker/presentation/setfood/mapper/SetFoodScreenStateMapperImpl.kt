package com.bavian.nyam.tracker.presentation.setfood.mapper

import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.model.StringSource
import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.presentation.setfood.SetFoodViewModelState as ViewModelState
import com.bavian.nyam.tracker.presentation.setfood.compose.SetFoodScreen.State as ScreenState

internal class SetFoodScreenStateMapperImpl : SetFoodScreenStateMapper {
    override fun map(state: ViewModelState): ScreenState {
        if (state.loading) {
            return ScreenState.Loading(
                canDelete = state.canDelete,
                deleteConfirmation = state.deleteConfirmation,
            )
        }

        val weightFloat = state.weight.toFloatOrNull() ?: 0f
        val factor = weightFloat / 100f
        return ScreenState.Success(
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
            kCalories = state.kCaloriesPer100 * factor,
            proteins = state.proteinsPer100 * factor,
            fat = state.fatPer100 * factor,
            carbohydrates = state.carbohydratesPer100 * factor,
            date = state.date,
            time = state.time,
            canDelete = state.canDelete,
            deleteConfirmation = state.deleteConfirmation,
        )
    }
}
