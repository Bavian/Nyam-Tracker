package com.bavian.nyam.tracker.presentation.setproduct

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SetProductViewModelState(
    val id: String,
    val manufacturer: String = "",
    val name: String = "",
    val calories: Float = 0f,
    val proteins: Float = 0f,
    val fat: Float = 0f,
    val carbohydrates: Float = 0f,
    val barcodes: ImmutableList<String> = persistentListOf(),
    val barcodeToDelete: String = "",
)
