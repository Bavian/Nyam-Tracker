package com.bavian.nyam.tracker.presentation.setweight.mapper

import com.bavian.nyam.tracker.presentation.setweight.SetWeightViewModelState
import com.bavian.nyam.tracker.presentation.setweight.compose.SetWeightScreen

internal interface SetWeightScreenStateMapper {
    fun map(state: SetWeightViewModelState): SetWeightScreen.State
}
