package com.bavian.nyam.tracker.presentation.productslist

import com.bavian.nyam.tracker.presentation.productslist.model.ProductsListProduct

sealed interface ProductsListEvent {
    data class SearchQueryChanged(
        val query: String,
    ) : ProductsListEvent

    data class ProductClicked(
        val product: ProductsListProduct,
    ) : ProductsListEvent

    data class EditProductClicked(
        val product: ProductsListProduct,
    ) : ProductsListEvent

    data class DeleteProductClicked(
        val product: ProductsListProduct,
    ) : ProductsListEvent

    data class ContextMenuClicked(
        val product: ProductsListProduct,
    ) : ProductsListEvent

    data object DismissContextMenu : ProductsListEvent

    data object DeleteProductConfirmed : ProductsListEvent

    data object DeleteProductCancelled : ProductsListEvent

    data object BackClicked : ProductsListEvent

    data object AddProductClicked : ProductsListEvent

    data object ScreenStarted : ProductsListEvent
}
