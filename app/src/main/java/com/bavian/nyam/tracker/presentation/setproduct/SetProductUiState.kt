package com.bavian.nyam.tracker.presentation.setproduct

data class SetProductUiState(
    val id: String? = null,
    val manufacturer: String = "",
    val name: String = "",
    val calories: String = "",
    val proteins: String = "",
    val fat: String = "",
    val carbohydrates: String = "",
    val isConfirmEnabled: Boolean = false,
)
