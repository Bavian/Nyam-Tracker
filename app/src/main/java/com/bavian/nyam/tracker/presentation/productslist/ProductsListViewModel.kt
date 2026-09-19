package com.bavian.nyam.tracker.presentation.productslist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.model.ProductSearchParams
import com.bavian.nyam.tracker.domain.usecase.GetProductsUseCase
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListProductMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getProductsUseCase: GetProductsUseCase,
    private val productMapper: ProductsListProductMapper,
    private val appNavigation: AppNavigation,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductsListUiState())
    val uiState: StateFlow<ProductsListUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
        observeNavigationResult()
    }

    private fun observeNavigationResult() {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<String?>("result", null).collect { result ->
                if (result != null) {
                    loadProducts()
                    savedStateHandle["result"] = null
                }
            }
        }
    }

    fun onEvent(event: ProductsListEvent) {
        when (event) {
            is ProductsListEvent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                loadProducts()
            }
            is ProductsListEvent.ProductClicked -> {
                // Handled in UI for Toast
            }
            is ProductsListEvent.EditProductClicked -> {
                openSetProductScreen(event.product.id)
            }
            is ProductsListEvent.ContextMenuClicked -> {
                _uiState.update { it.copy(expandedProductId = event.product.id) }
            }
            ProductsListEvent.DismissContextMenu -> {
                dismissContextMenu()
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            val domainProducts =
                getProductsUseCase.execute(
                    ProductSearchParams(key = _uiState.value.searchQuery),
                )
            val presentationProducts = domainProducts.map { productMapper.mapToPresentation(it) }
            _uiState.update { it.copy(products = presentationProducts) }
        }
    }

    private fun openSetProductScreen(productId: String) {
        dismissContextMenu()
        appNavigation.openSetProductScreen(productId)
    }

    private fun dismissContextMenu() {
        _uiState.update { it.copy(expandedProductId = null) }
    }
}
