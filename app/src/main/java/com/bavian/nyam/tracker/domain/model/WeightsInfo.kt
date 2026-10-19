package com.bavian.nyam.tracker.domain.model

import kotlinx.collections.immutable.ImmutableList

data class WeightsInfo(
    val items: ImmutableList<Item>,
) {
    data class Item(
        val weight: Float,
        val timestamp: Long,
    )
}
