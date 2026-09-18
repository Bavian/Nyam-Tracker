package com.bavian.nyam.tracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bavian.nyam.tracker.presentation.main.MainScreen
import com.bavian.nyam.tracker.presentation.main.MainUiState
import com.bavian.nyam.tracker.presentation.main.MainViewModel
import com.bavian.nyam.tracker.presentation.setproduct.SetProductScreen
import com.bavian.nyam.tracker.presentation.setproduct.SetProductUiState
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavGraph(
    navController: NavHostController,
    appNavigation: AppNavigation = koinInject(),
) {
    LaunchedEffect(Unit) {
        appNavigation.navigationActions.collect { action ->
            when (action) {
                is AppNavigation.NavigationAction.Back -> {
                    if (action.result != null) {
                        navController.previousBackStackEntry?.savedStateHandle?.set("result", action.result)
                    }
                    navController.popBackStack()
                }
                is AppNavigation.NavigationAction.Navigate -> navController.navigate(action.route.path)
                is AppNavigation.NavigationAction.NavigateWithTemplate -> navController.navigate(action.path)
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

        composable(
            route = AppRoute.SetProduct.routeWithArgs,
            arguments =
                listOf(
                    navArgument(AppRoute.SetProduct.ARG_PRODUCT_ID) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                ),
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString(AppRoute.SetProduct.ARG_PRODUCT_ID)
            val viewModel: SetProductViewModel = koinViewModel { parametersOf(productId) }
            SetProductScreen(
                state = viewModel.uiState.collectAsStateWithLifecycle(SetProductUiState()).value,
                onEvent = viewModel::onEvent,
            )
        }
    }
}
