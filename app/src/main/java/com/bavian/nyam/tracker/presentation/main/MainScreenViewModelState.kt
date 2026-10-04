package com.bavian.nyam.tracker.presentation.main

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class MainScreenViewModelState(
    val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val eatenFoodGroups: ImmutableList<Item> = persistentListOf(),
    val loading: Boolean = true,
) {
    data class Item(
        val id: String,
        val name: String,
        val manufacturer: String,
        val weight: Float,
        val calories: Float,
        val proteins: Float,
        val fat: Float,
        val carbohydrates: Float,
        val timestamp: Long,
    )
}
