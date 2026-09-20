package com.bavian.nyam.tracker.presentation.productslist

import com.bavian.nyam.tracker.presentation.productslist.model.ProductsListProduct
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ProductsListUiState(
    val searchQuery: String = "",
    val products: ImmutableList<ProductsListProduct> = persistentListOf(),
    val expandedProductId: String? = null,
    val deleteConfirmationProduct: ProductsListProduct? = null,
)
