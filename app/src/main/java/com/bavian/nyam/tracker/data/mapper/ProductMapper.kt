package com.bavian.nyam.tracker.data.mapper

import com.bavian.nyam.tracker.data.model.ProductEntity
import com.bavian.nyam.tracker.domain.model.Product

interface ProductMapper {
    fun mapToEntity(domain: Product): ProductEntity

    fun mapToDomain(entity: ProductEntity): Product
}
