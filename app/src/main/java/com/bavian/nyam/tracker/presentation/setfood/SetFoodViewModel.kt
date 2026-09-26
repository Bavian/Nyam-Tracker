package com.bavian.nyam.tracker.presentation.setfood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.infrastructure.ProductIdGenerator
import com.bavian.nyam.tracker.domain.model.EatenFood
import com.bavian.nyam.tracker.domain.usecase.AddEatenFoodUseCase
import com.bavian.nyam.tracker.domain.usecase.GetProductByIdUseCase
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class SetFoodViewModel(
    productId: String?,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addEatenFoodUseCase: AddEatenFoodUseCase,
    private val appNavigation: AppNavigation,
    private val productIdGenerator: ProductIdGenerator,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SetFoodViewModelState())
    val uiState: StateFlow<SetFoodViewModelState> = _uiState.asStateFlow()

    init {
        updateTitleText(_uiState.value.date, _uiState.value.time)
        if (productId != null) {
            loadProduct(productId)
        }
    }

    private fun loadProduct(id: String) {
        viewModelScope.launch {
            val product = getProductByIdUseCase.execute(id)
            if (product != null) {
                _uiState.update {
                    it.copy(
                        manufacturer = product.manufacturer,
                        name = product.name,
                        weight = "100",
                        kCalories = product.calories.toString(),
                        kCaloriesPer100 = product.calories,
                        proteins = product.proteins.toString(),
                        proteinsPer100 = product.proteins,
                        fat = product.fat.toString(),
                        fatPer100 = product.fat,
                        carbohydrates = product.carbohydrates.toString(),
                        carbohydratesPer100 = product.carbohydrates,
                    )
                }
            }
        }
    }

    fun onEvent(event: SetFoodViewModelEvent) {
        when (event) {
            is SetFoodViewModelEvent.ManufacturerChanged -> _uiState.update { it.copy(manufacturer = event.value) }
            is SetFoodViewModelEvent.NameChanged -> _uiState.update { it.copy(name = event.value) }
            is SetFoodViewModelEvent.WeightChanged -> updateWeight(event.value)
            is SetFoodViewModelEvent.CaloriesChanged -> _uiState.update { it.copy(kCalories = event.value) }
            is SetFoodViewModelEvent.ProteinsChanged -> _uiState.update { it.copy(proteins = event.value) }
            is SetFoodViewModelEvent.FatChanged -> _uiState.update { it.copy(fat = event.value) }
            is SetFoodViewModelEvent.CarbohydratesChanged -> _uiState.update { it.copy(carbohydrates = event.value) }
            is SetFoodViewModelEvent.DateChanged -> {
                _uiState.update { it.copy(date = event.value) }
                updateTitleText(_uiState.value.date, _uiState.value.time)
            }
            is SetFoodViewModelEvent.TimeChanged -> {
                _uiState.update { it.copy(time = event.value) }
                updateTitleText(_uiState.value.date, _uiState.value.time)
            }
            is SetFoodViewModelEvent.SavePressed -> saveFood()
            is SetFoodViewModelEvent.BackClicked -> appNavigation.back()
        }
    }

    private fun updateWeight(weight: String) {
        val newWeight = weight.toFloatOrNull() ?: return
        val factor = newWeight / 100f

        _uiState.update { state ->
            state.copy(
                weight = newWeight.toString(),
                kCalories = state.kCaloriesPer100.times(factor).toString(),
                proteins = state.proteinsPer100.times(factor).toString(),
                fat = state.fatPer100.times(factor).toString(),
                carbohydrates = state.carbohydratesPer100.times(factor).toString(),
            )
        }
    }

    private fun saveFood() {
        val state = _uiState.value
        viewModelScope.launch {
            val timestamp = combineDateAndTime(state.date, state.time)
            val eatenFood =
                EatenFood(
                    id = productIdGenerator.generateId(),
                    manufacturer = state.manufacturer,
                    name = state.name,
                    weight = state.weight.toFloatOrNull() ?: return@launch,
                    kCalories = state.kCalories.toFloatOrNull() ?: return@launch,
                    proteins = state.proteins.toFloatOrNull() ?: return@launch,
                    fat = state.fat.toFloatOrNull() ?: return@launch,
                    carbohydrates = state.carbohydrates.toFloatOrNull() ?: return@launch,
                    timestamp = timestamp,
                )
            addEatenFoodUseCase.execute(eatenFood)
            appNavigation.back()
        }
    }

    private fun combineDateAndTime(
        date: LocalDate?,
        time: LocalTime?,
    ): Long {
        val localDateTime =
            LocalDateTime(
                date?.year ?: 2000,
                date?.month?.number ?: 1,
                date?.day ?: 1,
                time?.hour ?: 0,
                time?.minute ?: 0,
                time?.second ?: 0,
            )
        return localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    private fun updateTitleText(
        date: LocalDate,
        time: LocalTime,
    ) {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val selectedDateTime = LocalDateTime(date, time)
        _uiState.update { it.copy(pastDay = currentDateTime > selectedDateTime) }
    }
}
