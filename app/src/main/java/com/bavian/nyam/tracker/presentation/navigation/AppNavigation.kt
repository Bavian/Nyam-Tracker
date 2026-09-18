package com.bavian.nyam.tracker.presentation.navigation

import kotlinx.coroutines.flow.SharedFlow

interface AppNavigation {
    val navigationActions: SharedFlow<NavigationAction>

    suspend fun startScan(): Result<Unit>

    fun back()

    fun openMainScreen()

    sealed interface NavigationAction {
        data object Back : NavigationAction

        data class Navigate(
            val route: AppRoute,
        ) : NavigationAction
    }
}
