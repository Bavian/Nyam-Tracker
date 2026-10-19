package com.bavian.nyam.tracker.data.mapper

import com.bavian.nyam.tracker.data.model.WeightEntity
import com.bavian.nyam.tracker.domain.model.WeightsInfo

class WeightMapperImpl : WeightMapper {
    override fun mapToEntity(domain: WeightsInfo.Item): WeightEntity =
        WeightEntity(
            weight = domain.weight,
            timestamp = domain.timestamp,
        )

    override fun mapToDomain(entity: WeightEntity): WeightsInfo.Item =
        WeightsInfo.Item(
            weight = entity.weight,
            timestamp = entity.timestamp,
        )
}
