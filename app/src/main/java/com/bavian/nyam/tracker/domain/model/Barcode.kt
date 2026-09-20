package com.bavian.nyam.tracker.domain.model

import kotlinx.collections.immutable.ImmutableList

data class Barcode(
    val id: String,
    val productIds: ImmutableList<String>,
)
