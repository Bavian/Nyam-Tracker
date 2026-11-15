package com.bavian.nyam.tracker.presentation.stats.mapper

import com.bavian.nyam.tracker.presentation.stats.StatsScreen
import com.bavian.nyam.tracker.presentation.stats.StatsViewModelEvent

interface StatsScreenEventMapper {
    fun map(event: StatsScreen.Event): StatsViewModelEvent
}
