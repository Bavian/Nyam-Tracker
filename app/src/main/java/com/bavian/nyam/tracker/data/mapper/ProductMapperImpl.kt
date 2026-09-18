package com.bavian.nyam.tracker.data.mapper

import com.bavian.nyam.tracker.data.model.ProductEntity
import com.bavian.nyam.tracker.domain.model.Product
import kotlin.math.roundToInt

class ProductMapperImpl : ProductMapper {
    override fun mapToEntity(domain: Product): ProductEntity =
        ProductEntity(
            id = domain.id,
            manufacturer = domain.manufacturer,
            name = domain.name,
            calories = domain.calories.times(100).roundToInt(),
            proteins = domain.proteins.times(100).roundToInt(),
            fat = domain.fat.times(100).roundToInt(),
            carbohydrates = domain.carbohydrates.times(100).roundToInt(),
        )

    override fun mapToDomain(entity: ProductEntity): Product =
        Product(
            id = entity.id,
            manufacturer = entity.manufacturer,
            name = entity.name,
            calories = entity.calories / 100f,
            proteins = entity.proteins / 100f,
            fat = entity.fat / 100f,
            carbohydrates = entity.carbohydrates / 100f,
        )
}
