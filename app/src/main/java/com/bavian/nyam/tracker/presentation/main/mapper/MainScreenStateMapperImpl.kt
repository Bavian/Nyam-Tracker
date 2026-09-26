package com.bavian.nyam.tracker.presentation.main.mapper

import com.bavian.nyam.tracker.presentation.main.MainScreen
import com.bavian.nyam.tracker.presentation.main.MainScreenViewModelState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

class MainScreenStateMapperImpl : MainScreenStateMapper {
    override fun map(state: MainScreenViewModelState): MainScreen.State {
        val timeZone = TimeZone.currentSystemDefault()

        val grouped =
            state.eatenFoodGroups.groupBy { item ->
                val localDateTime = Instant.fromEpochMilliseconds(item.timestamp).toLocalDateTime(timeZone)
                val startHour = localDateTime.hour
                val endHour = (startHour + 1) % 24
                val startStr = startHour.toString().padStart(2, '0') + ":00"
                val endStr = endHour.toString().padStart(2, '0') + ":00"
                "$startStr - $endStr"
            }

        val eatenFoodGroups =
            grouped
                .map { (title, items) ->
                    MainScreen.State.Success.EatenFoodGroup(
                        title = title,
                        items =
                            items
                                .map { item ->
                                    val localDateTime = Instant.fromEpochMilliseconds(item.timestamp).toLocalDateTime(timeZone)
                                    val timeStr =
                                        "${localDateTime.hour.toString().padStart(
                                            2,
                                            '0',
                                        )}:${localDateTime.minute.toString().padStart(2, '0')}"
                                    MainScreen.State.Success.EatenFoodGroup.Item(
                                        id = item.id,
                                        name = item.name,
                                        manufacturer = item.manufacturer,
                                        weight = formatNumber(item.weight),
                                        calories = formatNumber(item.calories),
                                        proteins = formatNumber(item.proteins),
                                        fat = formatNumber(item.fat),
                                        carbohydrates = formatNumber(item.carbohydrates),
                                        time = timeStr,
                                    )
                                }.toImmutableList(),
                    )
                }.toImmutableList()

        return MainScreen.State.Success(
            selectedDate = state.selectedDate,
            eatenFoodGroups = eatenFoodGroups,
        )
    }

    private fun formatNumber(value: Float): String =
        if ((value % 1f) == 0f) {
            value.toInt().toString()
        } else {
            value.toString()
        }
}
