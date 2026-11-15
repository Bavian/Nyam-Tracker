package com.bavian.nyam.tracker.presentation.stats.mapper

import com.bavian.nyam.tracker.presentation.stats.StatsScreen
import com.bavian.nyam.tracker.presentation.stats.StatsViewModelState

interface StatsScreenStateMapper {
    fun map(state: StatsViewModelState): StatsScreen.State
}
