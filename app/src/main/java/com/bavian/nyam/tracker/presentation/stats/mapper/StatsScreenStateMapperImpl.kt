package com.bavian.nyam.tracker.presentation.stats.mapper

import com.bavian.nyam.tracker.presentation.stats.StatsScreen
import com.bavian.nyam.tracker.presentation.stats.StatsViewModelState

class StatsScreenStateMapperImpl : StatsScreenStateMapper {
    override fun map(state: StatsViewModelState): StatsScreen.State {
        if (state.loading) {
            return StatsScreen.State.Loading(
                startDateDays = state.startDateDays,
                endDateDays = state.endDateDays,
            )
        }

        val dailyStats = state.dailyStats
        val averageCalories = if (dailyStats.isNotEmpty()) dailyStats.map { it.calories }.average().toFloat() else 0f
        val averageProteins = if (dailyStats.isNotEmpty()) dailyStats.map { it.proteins }.average().toFloat() else 0f
        val averageFats = if (dailyStats.isNotEmpty()) dailyStats.map { it.fats }.average().toFloat() else 0f
        val averageCarbohydrates = if (dailyStats.isNotEmpty()) dailyStats.map { it.carbohydrates }.average().toFloat() else 0f

        return StatsScreen.State.Success(
            startDateDays = state.startDateDays,
            endDateDays = state.endDateDays,
            dailyStats = dailyStats,
            averageCalories = averageCalories,
            averageProteins = averageProteins,
            averageFats = averageFats,
            averageCarbohydrates = averageCarbohydrates,
        )
    }
}
