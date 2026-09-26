package com.bavian.nyam.tracker.presentation.setfood

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class SetFoodViewModelState(
    val pastDay: Boolean = false,
    val manufacturer: String = "",
    val name: String = "",
    val weight: String = "100",
    val kCalories: String = "0",
    val kCaloriesPer100: Float = 0f,
    val proteins: String = "0",
    val proteinsPer100: Float = 0f,
    val fat: String = "0",
    val fatPer100: Float = 0f,
    val carbohydrates: String = "0",
    val carbohydratesPer100: Float = 0f,
    val date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val time: LocalTime =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .time,
)
