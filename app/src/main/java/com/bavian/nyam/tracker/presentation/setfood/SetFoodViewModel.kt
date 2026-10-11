package com.bavian.nyam.tracker.presentation.setfood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.infrastructure.ProductIdGenerator
import com.bavian.nyam.tracker.domain.model.EatenFood
import com.bavian.nyam.tracker.domain.usecase.AddEatenFoodUseCase
import com.bavian.nyam.tracker.domain.usecase.GetEatenFoodByIdUseCase
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
    private val getEatenFoodUseCase: GetEatenFoodByIdUseCase,
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
            val product = getEatenFoodUseCase.execute(id)
            if (product != null) {
                _uiState.update {
                    it.copy(
                        manufacturer = product.manufacturer,
                        name = product.name,
                        weight = "100",
                        kCaloriesPer100 = product.kCalories,
                        proteinsPer100 = product.proteins,
                        fatPer100 = product.fat,
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
            is SetFoodViewModelEvent.WeightChanged -> _uiState.update { it.copy(weight = event.value) }
            is SetFoodViewModelEvent.CaloriesChanged -> updateCalories(event.value)
            is SetFoodViewModelEvent.ProteinsChanged -> updateProteins(event.value)
            is SetFoodViewModelEvent.FatChanged -> updateFat(event.value)
            is SetFoodViewModelEvent.CarbohydratesChanged -> updateCarbohydrates(event.value)
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

    private fun updateCalories(calories: String) {
        val value = calories.toFloatOrNull() ?: 0f
        val weight = _uiState.value.weight.toFloatOrNull() ?: 100f
        val factor = weight / 100f
        val per100 = if (factor > 0f) value / factor else value
        _uiState.update { it.copy(kCaloriesPer100 = per100) }
    }

    private fun updateProteins(proteins: String) {
        val value = proteins.toFloatOrNull() ?: 0f
        val weight = _uiState.value.weight.toFloatOrNull() ?: 100f
        val factor = weight / 100f
        val per100 = if (factor > 0f) value / factor else value
        _uiState.update { it.copy(proteinsPer100 = per100) }
    }

    private fun updateFat(fat: String) {
        val value = fat.toFloatOrNull() ?: 0f
        val weight = _uiState.value.weight.toFloatOrNull() ?: 100f
        val factor = weight / 100f
        val per100 = if (factor > 0f) value / factor else value
        _uiState.update { it.copy(fatPer100 = per100) }
    }

    private fun updateCarbohydrates(carbohydrates: String) {
        val value = carbohydrates.toFloatOrNull() ?: 0f
        val weight = _uiState.value.weight.toFloatOrNull() ?: 100f
        val factor = weight / 100f
        val per100 = if (factor > 0f) value / factor else value
        _uiState.update { it.copy(carbohydratesPer100 = per100) }
    }

    private fun saveFood() {
        val state = _uiState.value
        viewModelScope.launch {
            val timestamp = combineDateAndTime(state.date, state.time)
            val weightFloat = state.weight.toFloatOrNull() ?: return@launch
            val factor = weightFloat / 100f
            val eatenFood =
                EatenFood(
                    id = productIdGenerator.generateId(),
                    manufacturer = state.manufacturer,
                    name = state.name,
                    weight = weightFloat,
                    kCalories = state.kCaloriesPer100 * factor,
                    proteins = state.proteinsPer100 * factor,
                    fat = state.fatPer100 * factor,
                    carbohydrates = state.carbohydratesPer100 * factor,
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
