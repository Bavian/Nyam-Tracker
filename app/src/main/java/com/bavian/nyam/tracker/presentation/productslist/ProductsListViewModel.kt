package com.bavian.nyam.tracker.presentation.productslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.model.ProductSearchParams
import com.bavian.nyam.tracker.domain.usecase.DeleteProductUseCase
import com.bavian.nyam.tracker.domain.usecase.GetProductsUseCase
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListProductMapper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val productMapper: ProductsListProductMapper,
    private val appNavigation: AppNavigation,
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
                openSetFoodScreen(event.product.id)
            }

            is ProductsListEvent.EditProductClicked -> {
                openSetProductScreen(event.product.id)
            }

            is ProductsListEvent.DeleteProductClicked -> {
                _uiState.update {
                    it.copy(
                        deleteConfirmationProduct = event.product,
                        expandedProductId = null,
                    )
                }
            }

            is ProductsListEvent.ContextMenuClicked -> {
                _uiState.update { it.copy(expandedProductId = event.product.id) }
            }

            ProductsListEvent.DismissContextMenu -> {
                dismissContextMenu()
            }

            ProductsListEvent.DeleteProductConfirmed -> {
                confirmDelete()
            }

            ProductsListEvent.DeleteProductCancelled -> {
                _uiState.update { it.copy(deleteConfirmationProduct = null) }
            }

            ProductsListEvent.BackClicked -> {
                appNavigation.back()
            }

            ProductsListEvent.AddProductClicked -> {
                appNavigation.openSetProductScreen()
            }

            ProductsListEvent.ScreenStarted -> {
                loadProducts()
            }
        }
    }

    private fun confirmDelete() {
        val product = _uiState.value.deleteConfirmationProduct ?: return
        viewModelScope.launch {
            deleteProductUseCase.execute(product.id)
            _uiState.update { it.copy(deleteConfirmationProduct = null) }
            loadProducts()
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            val domainProducts =
                getProductsUseCase.execute(
                    ProductSearchParams(key = _uiState.value.searchQuery),
                )
            val presentationProducts =
                domainProducts
                    .map { productMapper.mapToPresentation(it) }
                    .toImmutableList()
            _uiState.update { it.copy(products = presentationProducts) }
        }
    }

    private fun openSetProductScreen(productId: String) {
        dismissContextMenu()
        appNavigation.openSetProductScreen(productId)
    }

    private fun openSetFoodScreen(productId: String) {
        appNavigation.openAddFoodScreen(productId)
    }

    private fun dismissContextMenu() {
        _uiState.update { it.copy(expandedProductId = null) }
    }
}
