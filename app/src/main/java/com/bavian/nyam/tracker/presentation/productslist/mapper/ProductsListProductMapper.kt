package com.bavian.nyam.tracker.presentation.productslist.mapper

import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.presentation.productslist.model.ProductsListProduct

interface ProductsListProductMapper {
    fun mapToPresentation(domain: Product): ProductsListProduct
}
