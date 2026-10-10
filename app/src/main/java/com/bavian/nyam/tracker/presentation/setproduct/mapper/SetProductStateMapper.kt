package com.bavian.nyam.tracker.presentation.setproduct.mapper

import com.bavian.nyam.tracker.presentation.setproduct.SetProductScreen
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModelState

interface SetProductStateMapper {
    fun map(state: SetProductViewModelState): SetProductScreen.State
}
