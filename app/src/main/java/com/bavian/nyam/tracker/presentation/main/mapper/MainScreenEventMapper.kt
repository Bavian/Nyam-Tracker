package com.bavian.nyam.tracker.presentation.main.mapper

import com.bavian.nyam.tracker.presentation.main.MainScreen
import com.bavian.nyam.tracker.presentation.main.MainScreenViewModelEvent

interface MainScreenEventMapper {
    fun map(event: MainScreen.Event): MainScreenViewModelEvent
}
