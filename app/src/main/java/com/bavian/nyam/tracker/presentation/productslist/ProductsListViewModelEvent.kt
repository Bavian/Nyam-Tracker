package com.bavian.nyam.tracker.presentation.productslist

import com.bavian.nyam.tracker.presentation.productslist.model.ProductsListProduct

sealed interface ProductsListViewModelEvent {
    data class SearchQueryChanged(
        val query: String,
    ) : ProductsListViewModelEvent

    data class ProductClicked(
        val product: ProductsListProduct,
    ) : ProductsListViewModelEvent

    data class EditProductClicked(
        val product: ProductsListProduct,
    ) : ProductsListViewModelEvent

    data class DeleteProductClicked(
        val product: ProductsListProduct,
    ) : ProductsListViewModelEvent

    data class ContextMenuClicked(
        val product: ProductsListProduct,
    ) : ProductsListViewModelEvent

    data object DismissContextMenu : ProductsListViewModelEvent

    data object DeleteProductConfirmed : ProductsListViewModelEvent

    data object DeleteProductCancelled : ProductsListViewModelEvent

    data object BackClicked : ProductsListViewModelEvent

    data object AddProductClicked : ProductsListViewModelEvent

    data object ScreenStarted : ProductsListViewModelEvent
}
