package com.bavian.nyam.tracker.domain.model

data class EatenFood(
    val id: String,
    val manufacturer: String,
    val name: String,
    val weight: Float,
    val kCalories: Float,
    val proteins: Float,
    val fat: Float,
    val carbohydrates: Float,
    val timestamp: Long,
)
