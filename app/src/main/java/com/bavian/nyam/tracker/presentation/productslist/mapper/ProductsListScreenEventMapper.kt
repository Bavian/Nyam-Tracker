package com.bavian.nyam.tracker.presentation.productslist.mapper

import com.bavian.nyam.tracker.presentation.productslist.ProductsListScreen
import com.bavian.nyam.tracker.presentation.productslist.ProductsListViewModelEvent

interface ProductsListScreenEventMapper {
    fun map(event: ProductsListScreen.Event): ProductsListViewModelEvent
}
