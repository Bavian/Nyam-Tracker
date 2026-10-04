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
    private val _uiState = MutableStateFlow(ProductsListViewModelState())
    val uiState: StateFlow<ProductsListViewModelState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun onEvent(event: ProductsListViewModelEvent) {
        when (event) {
            is ProductsListViewModelEvent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                loadProducts()
            }

            is ProductsListViewModelEvent.ProductClicked -> {
                openSetFoodScreen(event.product.id)
            }

            is ProductsListViewModelEvent.EditProductClicked -> {
                openSetProductScreen(event.product.id)
            }

            is ProductsListViewModelEvent.DeleteProductClicked -> {
                _uiState.update {
                    it.copy(
                        deleteConfirmationProduct = event.product,
                        expandedProductId = null,
                    )
                }
            }

            is ProductsListViewModelEvent.ContextMenuClicked -> {
                _uiState.update { it.copy(expandedProductId = event.product.id) }
            }

            ProductsListViewModelEvent.DismissContextMenu -> {
                dismissContextMenu()
            }

            ProductsListViewModelEvent.DeleteProductConfirmed -> {
                confirmDelete()
            }

            ProductsListViewModelEvent.DeleteProductCancelled -> {
                _uiState.update { it.copy(deleteConfirmationProduct = null) }
            }

            ProductsListViewModelEvent.BackClicked -> {
                appNavigation.back()
            }

            ProductsListViewModelEvent.AddProductClicked -> {
                appNavigation.openSetProductScreen()
            }

            ProductsListViewModelEvent.ScreenStarted -> {
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
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            val domainProducts =
                getProductsUseCase.execute(
                    ProductSearchParams(key = _uiState.value.searchQuery),
                )
            val presentationProducts =
                domainProducts
                    .map { productMapper.mapToPresentation(it) }
                    .toImmutableList()
            _uiState.update {
                it.copy(
                    products = presentationProducts,
                    loading = false,
                )
            }
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
