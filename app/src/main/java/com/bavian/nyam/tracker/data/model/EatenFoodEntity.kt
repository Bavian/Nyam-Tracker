package com.bavian.nyam.tracker.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eaten_food")
data class EatenFoodEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "manufacturer")
    val manufacturer: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "weight")
    val weight: Int,
    @ColumnInfo(name = "kCalories")
    val kCalories: Int,
    @ColumnInfo(name = "proteins")
    val proteins: Int,
    @ColumnInfo(name = "fat")
    val fat: Int,
    @ColumnInfo(name = "carbohydrates")
    val carbohydrates: Int,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
)
