package com.bavian.nyam.tracker.presentation.productslist.mapper

import com.bavian.nyam.tracker.domain.model.Product
import com.bavian.nyam.tracker.presentation.productslist.model.ProductsListProduct

class ProductsListProductMapperImpl : ProductsListProductMapper {
    override fun mapToPresentation(domain: Product): ProductsListProduct =
        ProductsListProduct(
            id = domain.id,
            manufacturer = domain.manufacturer,
            name = domain.name,
            calories = domain.calories.toString(),
            proteins = domain.proteins.toString(),
            fat = domain.fat.toString(),
            carbohydrates = domain.carbohydrates.toString(),
        )
}
