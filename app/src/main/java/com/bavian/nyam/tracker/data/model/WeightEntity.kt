package com.bavian.nyam.tracker.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weights")
data class WeightEntity(
    @PrimaryKey
    val timestamp: Long,
    val weight: Float,
    @ColumnInfo(name = "last_modified_timestamp")
    val lastModifiedTimestamp: Long,
)
