package com.bavian.nyam.tracker.presentation.setproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.usecase.AddProductUseCase
import com.bavian.nyam.tracker.domain.usecase.GetProductByIdUseCase
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import com.bavian.nyam.tracker.presentation.setproduct.mapper.SetProductUiStateMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetProductViewModel(
    private val initialProductId: String?,
    private val addProductUseCase: AddProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val productMapper: SetProductUiStateMapper,
    private val appNavigation: AppNavigation,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SetProductUiState(id = initialProductId))
    val uiState: StateFlow<SetProductUiState> = _uiState.asStateFlow()

    init {
        loadProductIfIdExists()
    }

    private fun loadProductIfIdExists() {
        initialProductId?.let { id ->
            viewModelScope.launch {
                getProductByIdUseCase.execute(id)?.let { product ->
                    _uiState.update {
                        it.copy(
                            manufacturer = product.manufacturer,
                            name = product.name,
                            calories = product.calories.toString(),
                            proteins = product.proteins.toString(),
                            fat = product.fat.toString(),
                            carbohydrates = product.carbohydrates.toString(),
                            isConfirmEnabled = true,
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: SetProductEvent) {
        when (event) {
            is SetProductEvent.ManufacturerChanged -> {
                _uiState.update { it.copy(manufacturer = event.value) }
                validateForm()
            }
            is SetProductEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.value) }
                validateForm()
            }
            is SetProductEvent.CaloriesChanged -> {
                _uiState.update { it.copy(calories = event.value) }
                validateForm()
            }
            is SetProductEvent.ProteinsChanged -> {
                _uiState.update { it.copy(proteins = event.value) }
                validateForm()
            }
            is SetProductEvent.FatChanged -> {
                _uiState.update { it.copy(fat = event.value) }
                validateForm()
            }
            is SetProductEvent.CarbohydratesChanged -> {
                _uiState.update { it.copy(carbohydrates = event.value) }
                validateForm()
            }
            SetProductEvent.ConfirmClicked -> {
                confirmAddition()
            }
            SetProductEvent.BackClicked -> {
                appNavigation.back()
            }
        }
    }

    private fun validateForm() {
        val state = _uiState.value
        val isValid =
            state.manufacturer.isNotBlank() &&
                state.name.isNotBlank() &&
                state.calories.toFloatOrNull() != null &&
                state.proteins.toFloatOrNull() != null &&
                state.fat.toFloatOrNull() != null &&
                state.carbohydrates.toFloatOrNull() != null
        _uiState.update { it.copy(isConfirmEnabled = isValid) }
    }

    private fun confirmAddition() {
        val state = _uiState.value
        viewModelScope.launch {
            val domainProduct = productMapper.mapToDomain(state)
            addProductUseCase.execute(domainProduct)
            appNavigation.back(result = domainProduct.id)
        }
    }
}
