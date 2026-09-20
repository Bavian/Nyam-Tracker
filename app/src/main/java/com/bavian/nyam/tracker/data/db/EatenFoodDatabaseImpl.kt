package com.bavian.nyam.tracker.data.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.bavian.nyam.tracker.data.model.EatenFoodEntity

class EatenFoodDatabaseImpl(
    private val dbHelper: AppDatabaseHelper,
) : EatenFoodDatabase {
    override fun insertEatenFood(eatenFood: EatenFoodEntity) {
        val db = dbHelper.writableDatabase
        val values =
            ContentValues().apply {
                put(AppDatabaseHelper.COLUMN_EATEN_FOOD_ID, eatenFood.id)
                put(AppDatabaseHelper.COLUMN_EATEN_FOOD_MANUFACTURER, eatenFood.manufacturer)
                put(AppDatabaseHelper.COLUMN_EATEN_FOOD_NAME, eatenFood.name)
                put(AppDatabaseHelper.COLUMN_EATEN_FOOD_WEIGHT, eatenFood.weight)
                put(AppDatabaseHelper.COLUMN_EATEN_FOOD_KCALORIES, eatenFood.kCalories)
                put(AppDatabaseHelper.COLUMN_EATEN_FOOD_PROTEINS, eatenFood.proteins)
                put(AppDatabaseHelper.COLUMN_EATEN_FOOD_FAT, eatenFood.fat)
                put(AppDatabaseHelper.COLUMN_EATEN_FOOD_CARBOHYDRATES, eatenFood.carbohydrates)
                put(AppDatabaseHelper.COLUMN_EATEN_FOOD_TIMESTAMP, eatenFood.timestamp)
            }
        db.insertWithOnConflict(AppDatabaseHelper.TABLE_EATEN_FOOD, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun getEatenFoodForPeriod(period: LongRange): List<EatenFoodEntity> {
        val db = dbHelper.readableDatabase
        val cursor =
            db.query(
                AppDatabaseHelper.TABLE_EATEN_FOOD,
                null,
                "${AppDatabaseHelper.COLUMN_EATEN_FOOD_TIMESTAMP} BETWEEN ? AND ?",
                arrayOf(period.first.toString(), period.last.toString()),
                null,
                null,
                "${AppDatabaseHelper.COLUMN_EATEN_FOOD_TIMESTAMP} ASC",
            )
        val result = mutableListOf<EatenFoodEntity>()
        cursor.use {
            while (it.moveToNext()) {
                result.add(
                    EatenFoodEntity(
                        id = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EATEN_FOOD_ID)),
                        manufacturer = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EATEN_FOOD_MANUFACTURER)),
                        name = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EATEN_FOOD_NAME)),
                        weight = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EATEN_FOOD_WEIGHT)),
                        kCalories = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EATEN_FOOD_KCALORIES)),
                        proteins = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EATEN_FOOD_PROTEINS)),
                        fat = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EATEN_FOOD_FAT)),
                        carbohydrates = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EATEN_FOOD_CARBOHYDRATES)),
                        timestamp = it.getLong(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EATEN_FOOD_TIMESTAMP)),
                    ),
                )
            }
        }
        return result
    }
}
