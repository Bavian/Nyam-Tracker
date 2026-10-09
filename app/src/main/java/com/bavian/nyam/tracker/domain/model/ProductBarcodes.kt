package com.bavian.nyam.tracker.domain.model

import kotlinx.collections.immutable.ImmutableList

data class ProductBarcodes(
    val productId: String,
    val barcodes: ImmutableList<BarcodeInfo>,
)
