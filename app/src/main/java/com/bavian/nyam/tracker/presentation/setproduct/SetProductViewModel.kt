package com.bavian.nyam.tracker.presentation.setproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.infrastructure.ProductIdGenerator
import com.bavian.nyam.tracker.domain.model.BarcodeInfo
import com.bavian.nyam.tracker.domain.usecase.AddProductUseCase
import com.bavian.nyam.tracker.domain.usecase.DeleteBarcodeUseCase
import com.bavian.nyam.tracker.domain.usecase.GetBarcodesByProductIdUseCase
import com.bavian.nyam.tracker.domain.usecase.GetProductByIdUseCase
import com.bavian.nyam.tracker.domain.usecase.SetBarcodeUseCase
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScanner
import com.bavian.nyam.tracker.presentation.setproduct.mapper.SetProductUiStateMapper
import com.bavian.nyam.tracker.presentation.util.ErrorHandler
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetProductViewModel(
    private val initialProductId: String?,
    private val barcode: String?,
    private val addProductUseCase: AddProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val setBarcodeUseCase: SetBarcodeUseCase,
    private val getBarcodesByProductIdUseCase: GetBarcodesByProductIdUseCase,
    private val deleteBarcodeUseCase: DeleteBarcodeUseCase,
    private val barcodeScanner: BarcodeScanner,
    private val productMapper: SetProductUiStateMapper,
    productIdGenerator: ProductIdGenerator,
    private val appNavigation: AppNavigation,
    private val errorHandler: ErrorHandler,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            value =
                SetProductViewModelState(
                    id = initialProductId ?: productIdGenerator.generateId(),
                ),
        )
    val uiState: StateFlow<SetProductViewModelState> = _uiState.asStateFlow()

    init {
        loadProductIfIdExists()
    }

    fun onEvent(event: SetProductViewModelEvent) {
        when (event) {
            is SetProductViewModelEvent.ManufacturerChanged -> {
                _uiState.update { it.copy(manufacturer = event.value) }
            }

            is SetProductViewModelEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.value) }
            }

            is SetProductViewModelEvent.CaloriesChanged -> {
                _uiState.update { it.copy(calories = event.value.toFloatOrNull() ?: 0f) }
            }

            is SetProductViewModelEvent.ProteinsChanged -> {
                _uiState.update { it.copy(proteins = event.value.toFloatOrNull() ?: 0f) }
            }

            is SetProductViewModelEvent.FatChanged -> {
                _uiState.update { it.copy(fat = event.value.toFloatOrNull() ?: 0f) }
            }

            is SetProductViewModelEvent.CarbohydratesChanged -> {
                _uiState.update { it.copy(carbohydrates = event.value.toFloatOrNull() ?: 0f) }
            }

            is SetProductViewModelEvent.ConfirmClicked -> {
                confirmAddition()
            }

            is SetProductViewModelEvent.BackClicked -> {
                appNavigation.back()
            }

            is SetProductViewModelEvent.BarcodesPlusClicked -> {
                scanBarcode()
            }

            is SetProductViewModelEvent.BarcodeCrossClicked -> {
                _uiState.update { it.copy(barcodeToDelete = event.number) }
            }

            is SetProductViewModelEvent.DeleteBarcodeConfirmed -> {
                deleteBarcode()
            }

            is SetProductViewModelEvent.DeleteBarcodeCancelled -> {
                _uiState.update { it.copy(barcodeToDelete = "") }
            }

            is SetProductViewModelEvent.ScreenStarted -> {
                loadData()
            }
        }
    }

    private fun loadData() {
        loadBarcodes()
    }

    private fun loadProductIfIdExists() {
        initialProductId?.let { id ->
            viewModelScope.launch {
                getProductByIdUseCase.execute(id)?.let { product ->
                    _uiState.update {
                        it.copy(
                            manufacturer = product.manufacturer,
                            name = product.name,
                            calories = product.calories,
                            proteins = product.proteins,
                            fat = product.fat,
                            carbohydrates = product.carbohydrates,
                        )
                    }
                }
            }
        }
    }

    private fun loadBarcodes() {
        viewModelScope.launch {
            val productBarcodes = getBarcodesByProductIdUseCase.execute(uiState.value.id)
            _uiState.update { currentState ->
                currentState.copy(
                    barcodes = productBarcodes.barcodes.map { it.number }.toImmutableList(),
                )
            }
        }
    }

    private fun confirmAddition() {
        val state = _uiState.value
        viewModelScope.launch {
            val domainProduct = productMapper.mapToDomain(state)
            addProductUseCase.execute(domainProduct)

            if (!barcode.isNullOrBlank()) {
                saveBarcodeInfo(barcode, domainProduct.id)
            }

            appNavigation.back(result = domainProduct.id)
        }
    }

    private suspend fun saveBarcodeInfo(
        barcode: String,
        productId: String,
    ) {
        setBarcodeUseCase.execute(
            BarcodeInfo(
                number = barcode,
                productId = productId,
            ),
        )
    }

    private fun scanBarcode() {
        viewModelScope.launch {
            barcodeScanner
                .startScan()
                .onSuccess { scannerState ->
                    if (scannerState.barcode.isNotBlank()) {
                        setBarcodeUseCase.execute(
                            BarcodeInfo(
                                number = scannerState.barcode,
                                productId = uiState.value.id,
                            ),
                        )
                        loadBarcodes()
                    }
                }.onFailure(errorHandler::handleError)
        }
    }

    private fun deleteBarcode() {
        val barcodeToDelete = _uiState.value.barcodeToDelete
        if (barcodeToDelete != "") {
            viewModelScope.launch {
                deleteBarcodeUseCase(barcodeToDelete)
                _uiState.update { it.copy(barcodeToDelete = "") }
                loadBarcodes()
            }
        }
    }
}
