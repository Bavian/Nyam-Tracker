package com.bavian.nyam.tracker.presentation.navigation

import android.content.Context
import kotlinx.coroutines.flow.SharedFlow

interface AppNavigation {
    val navigationActions: SharedFlow<NavigationAction>

    fun startScan(context: Context)

    fun back()

    fun openMainScreen()

    sealed interface NavigationAction {
        data object Back : NavigationAction

        data class Navigate(
            val route: AppRoute,
        ) : NavigationAction
    }
}
