package com.bavian.nyam.tracker.presentation.setfood.mapper

import com.bavian.nyam.tracker.presentation.setfood.SetFoodViewModelState
import com.bavian.nyam.tracker.presentation.setfood.compose.SetFoodScreen

internal interface SetFoodScreenStateMapper {
    fun map(state: SetFoodViewModelState): SetFoodScreen.State
}
