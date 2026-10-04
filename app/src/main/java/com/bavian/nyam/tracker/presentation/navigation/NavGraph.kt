package com.bavian.nyam.tracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.bavian.nyam.tracker.presentation.main.MainScreen
import com.bavian.nyam.tracker.presentation.main.MainScreenViewModelState
import com.bavian.nyam.tracker.presentation.main.MainViewModel
import com.bavian.nyam.tracker.presentation.main.mapper.MainScreenEventMapper
import com.bavian.nyam.tracker.presentation.main.mapper.MainScreenStateMapper
import com.bavian.nyam.tracker.presentation.productslist.ProductsListScreen
import com.bavian.nyam.tracker.presentation.productslist.ProductsListViewModel
import com.bavian.nyam.tracker.presentation.productslist.ProductsListViewModelState
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListScreenEventMapper
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListScreenStateMapper
import com.bavian.nyam.tracker.presentation.setfood.SetFoodViewModel
import com.bavian.nyam.tracker.presentation.setfood.compose.SetFoodScreen
import com.bavian.nyam.tracker.presentation.setfood.mapper.SetFoodScreenEventMapper
import com.bavian.nyam.tracker.presentation.setfood.mapper.SetFoodScreenStateMapper
import com.bavian.nyam.tracker.presentation.setproduct.SetProductScreen
import com.bavian.nyam.tracker.presentation.setproduct.SetProductUiState
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavGraph(appNavigation: AppNavigation = koinInject()) {
    val navigationState =
        rememberNavigationState(
            startRoute = AppRoute.Main,
            topLevelRoutes = setOf(AppRoute.Main),
        )
    val navigator = remember(navigationState) { Navigator(navigationState) }

    LaunchedEffect(Unit) {
        appNavigation.navigationActions.collect { action ->
            when (action) {
                is AppNavigation.NavigationAction.Back -> {
                    navigator.goBack()
                }
                is AppNavigation.NavigationAction.Navigate -> {
                    navigator.navigate(action.route)
                }
            }
        }
    }

    val entryProvider =
        remember {
            entryProvider<NavKey> {
                entry<AppRoute.Main> {
                    val viewModel: MainViewModel = koinViewModel()
                    val stateMapper = koinInject<MainScreenStateMapper>()
                    val eventMapper = koinInject<MainScreenEventMapper>()
                    val state by viewModel.uiState.collectAsStateWithLifecycle(MainScreenViewModelState())
                    MainScreen(
                        state = stateMapper.map(state),
                        onEvent = { eventMapper.map(it).let(viewModel::onEvent) },
                    )
                }

                entry<AppRoute.SetProduct> { key ->
                    val viewModel: SetProductViewModel =
                        koinViewModel(key = "${key.productId}#${key.barcode}") { parametersOf(key.productId, key.barcode) }
                    SetProductScreen(
                        state = viewModel.uiState.collectAsStateWithLifecycle(SetProductUiState()).value,
                        onEvent = viewModel::onEvent,
                    )
                }

                entry<AppRoute.ProductsList> {
                    val viewModel: ProductsListViewModel = koinViewModel()
                    val stateMapper = koinInject<ProductsListScreenStateMapper>()
                    val eventMapper = koinInject<ProductsListScreenEventMapper>()
                    val state by viewModel.uiState.collectAsStateWithLifecycle(ProductsListViewModelState())
                    ProductsListScreen(
                        state = stateMapper.map(state),
                        onEvent = { eventMapper.map(it).let(viewModel::onEvent) },
                    )
                }

                entry<AppRoute.SetFood> { key ->
                    val viewModel: SetFoodViewModel = koinViewModel(key = key.productId) { parametersOf(key.productId) }
                    val stateMapper = koinInject<SetFoodScreenStateMapper>()
                    val eventMapper = koinInject<SetFoodScreenEventMapper>()
                    SetFoodScreen(
                        state = stateMapper.map(viewModel.uiState.collectAsStateWithLifecycle().value),
                        onEvent = { eventMapper.map(it).let(viewModel::onEvent) },
                    )
                }
            }
        }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
    )
}
