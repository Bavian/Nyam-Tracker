package com.bavian.nyam.tracker.domain.model

import kotlinx.collections.immutable.ImmutableList

data class BarcodeInfo(
    val number: String,
    val productIds: ImmutableList<String>,
)
