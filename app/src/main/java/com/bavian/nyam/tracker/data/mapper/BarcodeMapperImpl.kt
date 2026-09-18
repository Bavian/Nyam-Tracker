package com.bavian.nyam.tracker.data.mapper

import com.bavian.nyam.tracker.data.model.BarcodeInfoEntity
import com.bavian.nyam.tracker.domain.model.BarcodeInfo

class BarcodeMapperImpl : BarcodeMapper {
    override fun mapToEntity(domain: BarcodeInfo): BarcodeInfoEntity =
        BarcodeInfoEntity(
            number = domain.number,
            productIds = domain.productIds.joinToString(separator = ","),
        )

    override fun mapToDomain(entity: BarcodeInfoEntity): BarcodeInfo =
        BarcodeInfo(
            number = entity.number,
            productIds =
                if (entity.productIds.isEmpty()) {
                    emptyList()
                } else {
                    entity.productIds.split(",")
                },
        )
}
