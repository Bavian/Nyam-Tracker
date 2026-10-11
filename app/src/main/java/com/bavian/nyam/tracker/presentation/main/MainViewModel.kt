package com.bavian.nyam.tracker.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.usecase.DeleteEatenFoodUseCase
import com.bavian.nyam.tracker.domain.usecase.GetBarcodeInfoUseCase
import com.bavian.nyam.tracker.domain.usecase.GetEatenFoodUseCase
import com.bavian.nyam.tracker.presentation.main.mapper.MainScreenEatenFoodItemMapper
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScanner
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

class MainViewModel(
    private val appNavigation: AppNavigation,
    private val getEatenFoodUseCase: GetEatenFoodUseCase,
    private val deleteEatenFoodUseCase: DeleteEatenFoodUseCase,
    private val eatenFoodItemMapper: MainScreenEatenFoodItemMapper,
    private val barcodeScanner: BarcodeScanner,
    private val getBarcodeInfoUseCase: GetBarcodeInfoUseCase,
    private val errorHandler: ErrorHandler,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenViewModelState())
    val uiState: StateFlow<MainScreenViewModelState> = _uiState.asStateFlow()

    fun onEvent(event: MainScreenViewModelEvent) {
        when (event) {
            is MainScreenViewModelEvent.StartScanTap -> startScan()
            is MainScreenViewModelEvent.ProductsListTap -> appNavigation.openProductsListScreen()
            is MainScreenViewModelEvent.CalendarDatePicked -> {
                _uiState.update { it.copy(selectedDate = event.date) }
                loadEatenFood(event.date)
            }

            is MainScreenViewModelEvent.ScreenViewModelStarted -> loadEatenFood(uiState.value.selectedDate)
            is MainScreenViewModelEvent.EatenFoodTap -> openEditFoodScreen(event.foodId)
            is MainScreenViewModelEvent.EatenFoodEditTap -> openEditFoodScreen(event.foodId)
            is MainScreenViewModelEvent.EatenFoodDeleteTap -> deleteEatenFood(event.foodId)
        }
    }

    private fun deleteEatenFood(foodId: String) {
        viewModelScope.launch {
            deleteEatenFoodUseCase.execute(foodId)
            loadEatenFood(uiState.value.selectedDate)
        }
    }

    private fun loadEatenFood(date: LocalDate) {
        _uiState.update {
            it.copy(
                loading = true,
                selectedDate = date,
            )
        }

        viewModelScope.launch {
            val period = getPeriodForDate(date)
            val eatenFoodList = getEatenFoodUseCase.execute(period)
            val foodItems =
                eatenFoodList
                    .sortedBy { it.timestamp }
                    .map(eatenFoodItemMapper::map)
            _uiState.update {
                it.copy(
                    selectedDate = date,
                    eatenFoodGroups = foodItems.toImmutableList(),
                    loading = false,
                )
            }
        }
    }

    private fun getPeriodForDate(date: LocalDate): LongRange {
        val timeZone = TimeZone.currentSystemDefault()
        val startOfDay = date.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val startOfNextDay =
            (date + DatePeriod(days = 1)).atStartOfDayIn(timeZone).toEpochMilliseconds()
        return startOfDay until startOfNextDay
    }

    private fun startScan() {
        viewModelScope.launch {
            val scanResult = barcodeScanner.startScan()
            scanResult
                .onSuccess { scannerState ->
                    val barcodeInfo = getBarcodeInfoUseCase.execute(scannerState.barcode)
                    val productId = barcodeInfo?.productId
                    if (productId != null) {
                        appNavigation.openAddNewFoodScreen(productId)
                    } else {
                        appNavigation.openSetProductScreen(barcode = scannerState.barcode)
                    }
                }.onFailure(errorHandler::handleError)
        }
    }

    private fun openEditFoodScreen(id: String) {
        appNavigation.openEditFoodScreen(id)
    }
}
