package com.bavian.nyam.tracker.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barcodes")
data class BarcodeInfoEntity(
    @PrimaryKey
    val number: String,
    @ColumnInfo(name = "product_id")
    val productId: String,
)
