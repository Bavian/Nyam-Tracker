package com.bavian.nyam.tracker.presentation.main.mapper

import com.bavian.nyam.tracker.presentation.main.MainScreen
import com.bavian.nyam.tracker.presentation.main.MainScreenViewModelState

interface MainScreenStateMapper {
    fun map(state: MainScreenViewModelState): MainScreen.State
}
