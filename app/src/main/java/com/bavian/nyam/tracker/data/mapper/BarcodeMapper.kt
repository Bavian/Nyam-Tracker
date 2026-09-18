package com.bavian.nyam.tracker.data.mapper

import com.bavian.nyam.tracker.data.model.BarcodeInfoEntity
import com.bavian.nyam.tracker.domain.model.BarcodeInfo

interface BarcodeMapper {
    fun mapToEntity(domain: BarcodeInfo): BarcodeInfoEntity

    fun mapToDomain(entity: BarcodeInfoEntity): BarcodeInfo
}
