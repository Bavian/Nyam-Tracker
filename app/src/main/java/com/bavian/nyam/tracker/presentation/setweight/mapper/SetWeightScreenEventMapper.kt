package com.bavian.nyam.tracker.presentation.setweight.mapper

import com.bavian.nyam.tracker.presentation.setweight.SetWeightViewModelEvent
import com.bavian.nyam.tracker.presentation.setweight.compose.SetWeightScreen

internal interface SetWeightScreenEventMapper {
    fun map(event: SetWeightScreen.Event): SetWeightViewModelEvent
}
