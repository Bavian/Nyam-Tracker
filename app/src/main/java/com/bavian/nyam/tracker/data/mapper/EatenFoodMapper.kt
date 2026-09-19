package com.bavian.nyam.tracker.data.mapper

import com.bavian.nyam.tracker.data.model.EatenFoodEntity
import com.bavian.nyam.tracker.domain.model.EatenFood

interface EatenFoodMapper {
    fun mapToEntity(domain: EatenFood): EatenFoodEntity

    fun mapToDomain(entity: EatenFoodEntity): EatenFood
}
