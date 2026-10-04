package com.bavian.nyam.tracker.presentation.productslist.mapper

import com.bavian.nyam.tracker.presentation.productslist.ProductsListScreen
import com.bavian.nyam.tracker.presentation.productslist.ProductsListViewModelState

interface ProductsListScreenStateMapper {
    fun map(state: ProductsListViewModelState): ProductsListScreen.State
}
