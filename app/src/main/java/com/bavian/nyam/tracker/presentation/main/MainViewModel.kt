package com.bavian.nyam.tracker.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val appNavigation: AppNavigation,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.StartScanTap -> startScan()
            is MainScreenEvent.AddProductTap -> appNavigation.openSetProductScreen()
            is MainScreenEvent.ProductsListTap -> appNavigation.openProductsListScreen()
            is MainScreenEvent.CalendarDatePicked -> {
                _uiState.update { it.copy(selectedDate = event.date) }
            }
        }
    }

    fun startScan() {
        viewModelScope.launch {
            appNavigation.startScan()
        }
    }
}
