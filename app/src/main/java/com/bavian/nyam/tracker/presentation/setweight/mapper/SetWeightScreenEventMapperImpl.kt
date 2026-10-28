package com.bavian.nyam.tracker.presentation.setweight.mapper

import com.bavian.nyam.tracker.presentation.setweight.SetWeightViewModelEvent
import com.bavian.nyam.tracker.presentation.setweight.compose.SetWeightScreen

internal class SetWeightScreenEventMapperImpl : SetWeightScreenEventMapper {
    override fun map(event: SetWeightScreen.Event): SetWeightViewModelEvent =
        when (event) {
            is SetWeightScreen.Event.WeightChanged -> SetWeightViewModelEvent.WeightChanged(event.value)
            is SetWeightScreen.Event.DateChanged -> SetWeightViewModelEvent.DateChanged(event.value)
            is SetWeightScreen.Event.TimeChanged -> SetWeightViewModelEvent.TimeChanged(event.value)
            is SetWeightScreen.Event.SavePressed -> SetWeightViewModelEvent.SavePressed
            is SetWeightScreen.Event.BackClicked -> SetWeightViewModelEvent.BackClicked
            is SetWeightScreen.Event.DeleteClicked -> SetWeightViewModelEvent.DeleteClicked
            is SetWeightScreen.Event.DeleteConfirmed -> SetWeightViewModelEvent.DeleteConfirmed
            is SetWeightScreen.Event.DeleteCancelled -> SetWeightViewModelEvent.DeleteCancelled
        }
}
