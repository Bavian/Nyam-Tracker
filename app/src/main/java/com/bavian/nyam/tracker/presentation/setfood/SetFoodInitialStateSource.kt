package com.bavian.nyam.tracker.presentation.setfood

sealed interface SetFoodInitialStateSource {
    data class Product(
        val id: String,
    ) : SetFoodInitialStateSource

    data class Food(
        val id: String,
    ) : SetFoodInitialStateSource

    data object None : SetFoodInitialStateSource
}
