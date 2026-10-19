package com.bavian.nyam.tracker.data.mapper

import com.bavian.nyam.tracker.data.model.WeightEntity
import com.bavian.nyam.tracker.domain.model.WeightsInfo

interface WeightMapper {
    fun mapToEntity(domain: WeightsInfo.Item): WeightEntity

    fun mapToDomain(entity: WeightEntity): WeightsInfo.Item
}
