package com.bavian.nyam.tracker.data.mapper

import com.bavian.nyam.tracker.data.model.EatenFoodEntity
import com.bavian.nyam.tracker.domain.model.EatenFood
import kotlin.math.roundToInt

class EatenFoodMapperImpl : EatenFoodMapper {
    override fun mapToEntity(domain: EatenFood): EatenFoodEntity =
        EatenFoodEntity(
            id = domain.id,
            manufacturer = domain.manufacturer,
            name = domain.name,
            kCalories = domain.kCalories.times(100).roundToInt(),
            proteins = domain.proteins.times(100).roundToInt(),
            fat = domain.fat.times(100).roundToInt(),
            carbohydrates = domain.carbohydrates.times(100).roundToInt(),
            timestamp = domain.timestamp,
        )

    override fun mapToDomain(entity: EatenFoodEntity): EatenFood =
        EatenFood(
            id = entity.id,
            manufacturer = entity.manufacturer,
            name = entity.name,
            kCalories = entity.kCalories / 100f,
            proteins = entity.proteins / 100f,
            fat = entity.fat / 100f,
            carbohydrates = entity.carbohydrates / 100f,
            timestamp = entity.timestamp,
        )
}
