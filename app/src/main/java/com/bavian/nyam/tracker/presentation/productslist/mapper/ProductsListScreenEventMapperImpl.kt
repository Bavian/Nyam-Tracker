package com.bavian.nyam.tracker.presentation.productslist.mapper

import com.bavian.nyam.tracker.presentation.productslist.ProductsListScreen
import com.bavian.nyam.tracker.presentation.productslist.ProductsListViewModelEvent

class ProductsListScreenEventMapperImpl : ProductsListScreenEventMapper {
    override fun map(event: ProductsListScreen.Event): ProductsListViewModelEvent =
        when (event) {
            is ProductsListScreen.Event.SearchQueryChanged -> ProductsListViewModelEvent.SearchQueryChanged(event.query)
            is ProductsListScreen.Event.ProductClicked -> ProductsListViewModelEvent.ProductClicked(event.product)
            is ProductsListScreen.Event.EditProductClicked -> ProductsListViewModelEvent.EditProductClicked(event.product)
            is ProductsListScreen.Event.DeleteProductClicked -> ProductsListViewModelEvent.DeleteProductClicked(event.product)
            is ProductsListScreen.Event.ContextMenuClicked -> ProductsListViewModelEvent.ContextMenuClicked(event.product)
            is ProductsListScreen.Event.DismissContextMenu -> ProductsListViewModelEvent.DismissContextMenu
            is ProductsListScreen.Event.DeleteProductConfirmed -> ProductsListViewModelEvent.DeleteProductConfirmed
            is ProductsListScreen.Event.DeleteProductCancelled -> ProductsListViewModelEvent.DeleteProductCancelled
            is ProductsListScreen.Event.BackClicked -> ProductsListViewModelEvent.BackClicked
            is ProductsListScreen.Event.AddProductClicked -> ProductsListViewModelEvent.AddProductClicked
            is ProductsListScreen.Event.ScreenStarted -> ProductsListViewModelEvent.ScreenStarted
        }
}
