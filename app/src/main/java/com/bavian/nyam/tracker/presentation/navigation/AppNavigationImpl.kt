package com.bavian.nyam.tracker.presentation.navigation

import com.bavian.nyam.tracker.presentation.navigation.AppNavigation.NavigationAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppNavigationImpl : AppNavigation {
    private val _navigationActions = MutableSharedFlow<NavigationAction>(extraBufferCapacity = 1)
    override val navigationActions: SharedFlow<NavigationAction> = _navigationActions.asSharedFlow()

    override fun back(result: Any?) {
        _navigationActions.tryEmit(NavigationAction.Back(result))
    }

    override fun openMainScreen() {
        _navigationActions.tryEmit(NavigationAction.Navigate(AppRoute.Main))
    }

    override fun openSetProductScreen(
        productId: String?,
        barcode: String?,
    ) {
        _navigationActions.tryEmit(NavigationAction.Navigate(AppRoute.SetProduct(productId, barcode)))
    }

    override fun openProductsListScreen() {
        _navigationActions.tryEmit(NavigationAction.Navigate(AppRoute.ProductsList))
    }

    override fun openAddFoodScreen(productId: String?) {
        _navigationActions.tryEmit(NavigationAction.Navigate(AppRoute.SetFood(productId)))
    }
}
