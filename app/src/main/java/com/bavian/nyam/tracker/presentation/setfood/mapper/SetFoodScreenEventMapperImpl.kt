package com.bavian.nyam.tracker.presentation.setfood.mapper

import com.bavian.nyam.tracker.presentation.setfood.SetFoodViewModelEvent
import com.bavian.nyam.tracker.presentation.setfood.compose.SetFoodScreen

internal class SetFoodScreenEventMapperImpl : SetFoodScreenEventMapper {
    override fun map(event: SetFoodScreen.Event): SetFoodViewModelEvent =
        when (event) {
            is SetFoodScreen.Event.ManufacturerChanged -> SetFoodViewModelEvent.ManufacturerChanged(event.value)
            is SetFoodScreen.Event.NameChanged -> SetFoodViewModelEvent.NameChanged(event.value)
            is SetFoodScreen.Event.WeightChanged -> SetFoodViewModelEvent.WeightChanged(event.value)
            is SetFoodScreen.Event.CaloriesChanged -> SetFoodViewModelEvent.CaloriesChanged(event.value)
            is SetFoodScreen.Event.ProteinsChanged -> SetFoodViewModelEvent.ProteinsChanged(event.value)
            is SetFoodScreen.Event.FatChanged -> SetFoodViewModelEvent.FatChanged(event.value)
            is SetFoodScreen.Event.CarbohydratesChanged -> SetFoodViewModelEvent.CarbohydratesChanged(event.value)
            is SetFoodScreen.Event.DateChanged -> SetFoodViewModelEvent.DateChanged(event.value)
            is SetFoodScreen.Event.TimeChanged -> SetFoodViewModelEvent.TimeChanged(event.value)
            is SetFoodScreen.Event.SavePressed -> SetFoodViewModelEvent.SavePressed
            is SetFoodScreen.Event.BackClicked -> SetFoodViewModelEvent.BackClicked
        }
}
