package com.bavian.nyam.tracker.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.usecase.GetEatenFoodUseCase
import com.bavian.nyam.tracker.presentation.main.mapper.MainScreenEatenFoodItemMapper
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
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

class MainViewModel(
    private val appNavigation: AppNavigation,
    private val getEatenFoodUseCase: GetEatenFoodUseCase,
    private val eatenFoodItemMapper: MainScreenEatenFoodItemMapper,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenViewModelState())
    val uiState: StateFlow<MainScreenViewModelState> = _uiState.asStateFlow()

    init {
        loadEatenFood()
    }

    fun onEvent(event: MainScreenViewModelEvent) {
        when (event) {
            is MainScreenViewModelEvent.StartScanTap -> startScan()
            is MainScreenViewModelEvent.ProductsListTap -> appNavigation.openProductsListScreen()
            is MainScreenViewModelEvent.CalendarDatePicked -> {
                _uiState.update { it.copy(selectedDate = event.date) }
                loadEatenFood()
            }

            is MainScreenViewModelEvent.ScreenViewModelStarted -> loadEatenFood()
            is MainScreenViewModelEvent.EatenFoodTap -> appNavigation.openAddFoodScreen(event.foodId)
        }
    }

    private fun loadEatenFood() {
        val selectedDate = _uiState.value.selectedDate
        viewModelScope.launch {
            val period = getPeriodForDate(selectedDate)
            val eatenFoodList = getEatenFoodUseCase.execute(period)
            val foodItems =
                eatenFoodList
                    .sortedBy { it.timestamp }
                    .map(eatenFoodItemMapper::map)
            _uiState.update { it.copy(eatenFoodGroups = foodItems.toImmutableList()) }
        }
    }

    private fun getPeriodForDate(date: LocalDate): LongRange {
        val timeZone = TimeZone.currentSystemDefault()
        val startOfDay = date.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val startOfNextDay =
            (date + DatePeriod(days = 1)).atStartOfDayIn(timeZone).toEpochMilliseconds()
        return startOfDay until startOfNextDay
    }

    fun startScan() {
        viewModelScope.launch {
            appNavigation.startScan()
        }
    }
}
