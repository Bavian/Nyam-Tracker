package com.bavian.nyam.tracker.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute : NavKey {
    @Serializable
    data object Main : AppRoute

    @Serializable
    data class SetProduct(
        val productId: String? = null,
        val barcode: String? = null,
    ) : AppRoute

    @Serializable
    data object ProductsList : AppRoute

    @Serializable
    data class SetFood(
        val productId: String? = null,
    ) : AppRoute
}
