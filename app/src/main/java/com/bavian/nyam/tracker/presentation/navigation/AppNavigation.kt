package com.bavian.nyam.tracker.presentation.navigation

import kotlinx.coroutines.flow.SharedFlow

interface AppNavigation {
    val navigationActions: SharedFlow<NavigationAction>

    suspend fun startScan(): Result<Unit>

    fun back(result: Any? = null)

    fun openMainScreen()

    fun openSetProductScreen(productId: String? = null)

    fun openProductsListScreen()

    fun openAddFoodScreen(productId: String? = null)

    sealed interface NavigationAction {
        data class Back(
            val result: Any? = null,
        ) : NavigationAction

        data class Navigate(
            val route: AppRoute,
        ) : NavigationAction

        data class NavigateWithTemplate(
            val path: String,
        ) : NavigationAction
    }
}
