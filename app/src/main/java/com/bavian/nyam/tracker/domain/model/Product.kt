package com.bavian.nyam.tracker.domain.model

data class Product(
    val id: String,
    val manufacturer: String,
    val name: String,
    val calories: Float,
    val proteins: Float,
    val fat: Float,
    val carbohydrates: Float,
)
