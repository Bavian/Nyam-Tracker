package com.bavian.nyam.tracker.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(
    context: Context,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "nyam_tracker.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_EATEN_FOOD = "eaten_food"
        const val COLUMN_EATEN_FOOD_ID = "id"
        const val COLUMN_EATEN_FOOD_MANUFACTURER = "manufacturer"
        const val COLUMN_EATEN_FOOD_NAME = "name"
        const val COLUMN_EATEN_FOOD_WEIGHT = "weight"
        const val COLUMN_EATEN_FOOD_KCALORIES = "kcalories"
        const val COLUMN_EATEN_FOOD_PROTEINS = "proteins"
        const val COLUMN_EATEN_FOOD_FAT = "fat"
        const val COLUMN_EATEN_FOOD_CARBOHYDRATES = "carbohydrates"
        const val COLUMN_EATEN_FOOD_TIMESTAMP = "timestamp"

        private val CREATE_EATEN_FOOD_TABLE =
            """
            CREATE TABLE $TABLE_EATEN_FOOD (
                $COLUMN_EATEN_FOOD_ID TEXT PRIMARY KEY,
                $COLUMN_EATEN_FOOD_MANUFACTURER TEXT,
                $COLUMN_EATEN_FOOD_NAME TEXT,
                $COLUMN_EATEN_FOOD_WEIGHT INTEGER,
                $COLUMN_EATEN_FOOD_KCALORIES INTEGER,
                $COLUMN_EATEN_FOOD_PROTEINS INTEGER,
                $COLUMN_EATEN_FOOD_FAT INTEGER,
                $COLUMN_EATEN_FOOD_CARBOHYDRATES INTEGER,
                $COLUMN_EATEN_FOOD_TIMESTAMP INTEGER
            )
            """.trimIndent()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_EATEN_FOOD_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int,
    ) {
        // No migration logic needed since app wasn't released
    }
}
