package com.bavian.nyam.tracker.presentation.setproduct.mapper

import com.bavian.nyam.tracker.presentation.setproduct.SetProductScreen
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModelEvent

interface SetProductEventMapper {
    fun map(event: SetProductScreen.Event): SetProductViewModelEvent
}
