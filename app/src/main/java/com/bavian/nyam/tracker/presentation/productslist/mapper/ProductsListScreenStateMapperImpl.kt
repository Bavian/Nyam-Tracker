package com.bavian.nyam.tracker.presentation.productslist.mapper

import com.bavian.nyam.tracker.presentation.productslist.ProductsListScreen
import com.bavian.nyam.tracker.presentation.productslist.ProductsListViewModelState

class ProductsListScreenStateMapperImpl : ProductsListScreenStateMapper {
    override fun map(state: ProductsListViewModelState): ProductsListScreen.State {
        if (state.loading) {
            return ProductsListScreen.State.Loading(
                searchQuery = state.searchQuery,
                expandedProductId = state.expandedProductId,
                deleteConfirmationProduct = state.deleteConfirmationProduct,
            )
        }

        return ProductsListScreen.State.Success(
            searchQuery = state.searchQuery,
            products = state.products,
            expandedProductId = state.expandedProductId,
            deleteConfirmationProduct = state.deleteConfirmationProduct,
        )
    }
}
