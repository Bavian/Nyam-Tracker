package com.bavian.nyam.tracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bavian.nyam.tracker.presentation.main.MainScreen
import org.koin.compose.koinInject

@Composable
fun AppNavGraph(
    navController: NavHostController,
    appNavigation: AppNavigation = koinInject(),
) {
    LaunchedEffect(Unit) {
        appNavigation.navigationActions.collect { action ->
            when (action) {
                is AppNavigation.NavigationAction.Back -> navController.popBackStack()
                is AppNavigation.NavigationAction.Navigate -> navController.navigate(action.route.path)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoute.Main.path,
    ) {
        composable(AppRoute.Main.path) {
            MainScreen()
        }
    }
}
