package com.bavian.nyam.tracker.presentation.productslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.model.ProductSearchParams
import com.bavian.nyam.tracker.domain.usecase.GetProductsUseCase
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListProductMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val productMapper: ProductsListProductMapper,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductsListUiState())
    val uiState: StateFlow<ProductsListUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun onEvent(event: ProductsListEvent) {
        when (event) {
            is ProductsListEvent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                loadProducts()
            }
            is ProductsListEvent.ProductClicked -> {
                // Handled in UI for Toast for now
            }
            is ProductsListEvent.ContextMenuClicked -> {
                // Not implemented yet
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
}
