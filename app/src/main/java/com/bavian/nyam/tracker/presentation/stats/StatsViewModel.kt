package com.bavian.nyam.tracker.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.usecase.eatenfood.GetEatenFoodUseCase
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import com.bavian.nyam.tracker.presentation.util.ErrorHandler
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

class StatsViewModel(
    private val getEatenFoodUseCase: GetEatenFoodUseCase,
    private val appNavigation: AppNavigation,
    private val errorHandler: ErrorHandler,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatsViewModelState())
    val uiState: StateFlow<StatsViewModelState> = _uiState.asStateFlow()

    fun onEvent(event: StatsViewModelEvent) {
        when (event) {
            is StatsViewModelEvent.DateRangePicked -> {
                _uiState.update {
                    it.copy(
                        startDateDays = minOf(event.startDateDays, event.endDateDays),
                        endDateDays = maxOf(event.startDateDays, event.endDateDays),
                    )
                }
                loadData()
            }

            is StatsViewModelEvent.BackClicked -> {
                appNavigation.back()
            }

            is StatsViewModelEvent.ScreenStarted -> {
                loadData()
            }
        }
    }

    private fun loadData() {
        val startDays = _uiState.value.startDateDays
        val endDays = _uiState.value.endDateDays

        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            try {
                val timeZone = TimeZone.currentSystemDefault()
                val startLocalDate = LocalDate.fromEpochDays(startDays)
                val endLocalDate = LocalDate.fromEpochDays(endDays)

                val startMillis = startLocalDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
                val endMillis = (endLocalDate + DatePeriod(days = 1)).atStartOfDayIn(timeZone).toEpochMilliseconds()

                val eatenFoodList = getEatenFoodUseCase.execute(startMillis until endMillis)

                val eatenFoodByDay =
                    eatenFoodList.groupBy { food ->
                        val foodLocalDate =
                            Instant
                                .fromEpochMilliseconds(food.timestamp)
                                .toLocalDateTime(timeZone)
                                .date
                        foodLocalDate.toEpochDays()
                    }

                val dailyStatsList =
                    (startDays..endDays)
                        .map { day ->
                            val dayFoods = eatenFoodByDay[day] ?: emptyList()
                            StatsScreen.DailyStat(
                                dateDays = day,
                                calories = dayFoods.sumOf { it.kCalories.toDouble() }.toFloat(),
                                proteins = dayFoods.sumOf { it.proteins.toDouble() }.toFloat(),
                                fats = dayFoods.sumOf { it.fat.toDouble() }.toFloat(),
                                carbohydrates = dayFoods.sumOf { it.carbohydrates.toDouble() }.toFloat(),
                            )
                        }.toImmutableList()

                _uiState.update {
                    it.copy(
                        dailyStats = dailyStatsList,
                        loading = false,
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false) }
                errorHandler.handleError(e)
            }
        }
    }
}
