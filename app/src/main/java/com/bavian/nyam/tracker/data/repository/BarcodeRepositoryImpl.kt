package com.bavian.nyam.tracker.data.repository

import com.bavian.nyam.tracker.data.db.BarcodeDatabase
import com.bavian.nyam.tracker.data.mapper.BarcodeMapper
import com.bavian.nyam.tracker.domain.model.BarcodeInfo
import com.bavian.nyam.tracker.domain.repository.BarcodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BarcodeRepositoryImpl(
    private val barcodeDatabase: BarcodeDatabase,
    private val barcodeMapper: BarcodeMapper,
) : BarcodeRepository {
    override suspend fun setBarcode(barcode: BarcodeInfo) =
        withContext(Dispatchers.IO) {
            val entity = barcodeMapper.mapToEntity(barcode)
            barcodeDatabase.insertBarcode(entity)
        }

    override suspend fun getBarcodeByNumber(number: String): BarcodeInfo? =
        withContext(Dispatchers.IO) {
            val entity = barcodeDatabase.getBarcodeByNumber(number)
            entity?.let { barcodeMapper.mapToDomain(it) }
        }
}
