package com.bavian.nyam.tracker.presentation.navigation

import kotlinx.coroutines.flow.SharedFlow

interface AppNavigation {
    val navigationActions: SharedFlow<NavigationAction>

    fun back(result: Any? = null)

    fun openMainScreen()

    fun openSetProductScreen(
        productId: String? = null,
        barcode: String? = null,
    )

    fun openProductsListScreen()

    fun openAddFoodScreen(productId: String? = null)

    sealed interface NavigationAction {
        data class Back(
            val result: Any? = null,
        ) : NavigationAction

        data class Navigate(
            val route: AppRoute,
        ) : NavigationAction
    }
}
