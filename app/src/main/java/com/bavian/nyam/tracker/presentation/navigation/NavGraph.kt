package com.bavian.nyam.tracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bavian.nyam.tracker.presentation.main.MainScreen
import com.bavian.nyam.tracker.presentation.main.MainUiState
import com.bavian.nyam.tracker.presentation.main.MainViewModel
import org.koin.androidx.compose.koinViewModel
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
            val viewModel: MainViewModel = koinViewModel()
            MainScreen(
                state = viewModel.uiState.collectAsStateWithLifecycle(MainUiState()).value,
                onEvent = viewModel::onEvent,
            )
        }
    }
}
