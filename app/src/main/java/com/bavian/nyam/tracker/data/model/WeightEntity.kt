package com.bavian.nyam.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weights")
data class WeightEntity(
    @PrimaryKey
    val timestamp: Long,
    val weight: Float,
)
