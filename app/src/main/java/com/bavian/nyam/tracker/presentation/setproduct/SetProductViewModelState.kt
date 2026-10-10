package com.bavian.nyam.tracker.presentation.setproduct

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SetProductViewModelState(
    val id: String,
    val manufacturer: String = "",
    val name: String = "",
    val calories: String = "",
    val proteins: String = "",
    val fat: String = "",
    val carbohydrates: String = "",
    val barcodes: ImmutableList<String> = persistentListOf(),
    val barcodeToDelete: String = "",
)
