package com.bavian.nyam.tracker.presentation.setweight.mapper

import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.presentation.setweight.SetWeightViewModelState as ViewModelState
import com.bavian.nyam.tracker.presentation.setweight.compose.SetWeightScreen.State as ScreenState

internal class SetWeightScreenStateMapperImpl : SetWeightScreenStateMapper {
    override fun map(state: ViewModelState): ScreenState {
        if (state.loading) {
            return ScreenState.Loading(
                canDelete = state.canDelete,
                deleteConfirmation = state.deleteConfirmation,
            )
        }

        return ScreenState.Success(
            weight = state.weight.stringSource,
            date = state.date,
            time = state.time,
            canDelete = state.canDelete,
            deleteConfirmation = state.deleteConfirmation,
        )
    }
}
