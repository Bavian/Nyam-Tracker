package com.bavian.nyam.tracker.data.model

data class EatenFoodEntity(
    val id: String,
    val manufacturer: String,
    val name: String,
    val weight: Int,
    val kCalories: Int,
    val proteins: Int,
    val fat: Int,
    val carbohydrates: Int,
    val timestamp: Long,
)
