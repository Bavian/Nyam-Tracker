package com.bavian.nyam.tracker.presentation.setfood.mapper

import com.bavian.nyam.tracker.presentation.setfood.SetFoodViewModelEvent
import com.bavian.nyam.tracker.presentation.setfood.compose.SetFoodScreen

internal interface SetFoodScreenEventMapper {
    fun map(event: SetFoodScreen.Event): SetFoodViewModelEvent
}
