package com.bavian.nyam.tracker.presentation.stats.mapper

import com.bavian.nyam.tracker.presentation.stats.StatsScreen
import com.bavian.nyam.tracker.presentation.stats.StatsViewModelEvent

class StatsScreenEventMapperImpl : StatsScreenEventMapper {
    override fun map(event: StatsScreen.Event): StatsViewModelEvent =
        when (event) {
            is StatsScreen.Event.DateRangePicked ->
                StatsViewModelEvent.DateRangePicked(
                    startDateDays = event.startDateDays,
                    endDateDays = event.endDateDays,
                )

            is StatsScreen.Event.BackClicked ->
                StatsViewModelEvent.BackClicked

            is StatsScreen.Event.ScreenStarted ->
                StatsViewModelEvent.ScreenStarted
        }
}
