package com.bavian.nyam.tracker.data.model

data class ProductEntity(
    val id: String,
    val manufacturer: String,
    val name: String,
    val calories: Int,
    val proteins: Int,
    val fat: Int,
    val carbohydrates: Int,
)
