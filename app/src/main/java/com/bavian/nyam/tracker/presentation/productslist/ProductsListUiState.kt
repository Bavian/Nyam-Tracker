package com.bavian.nyam.tracker.presentation.productslist

import com.bavian.nyam.tracker.presentation.productslist.model.ProductsListProduct

data class ProductsListUiState(
    val searchQuery: String = "",
    val products: List<ProductsListProduct> = emptyList(),
    val expandedProductId: String? = null,
    val deleteConfirmationProduct: ProductsListProduct? = null,
)
