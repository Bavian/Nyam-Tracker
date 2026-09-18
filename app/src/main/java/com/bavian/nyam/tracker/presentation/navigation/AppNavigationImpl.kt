package com.bavian.nyam.tracker.presentation.navigation

import android.content.Context
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation.NavigationAction
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScanner
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppNavigationImpl(
    private val context: Context,
    private val barcodeScanner: BarcodeScanner,
) : AppNavigation {
    private val _navigationActions = MutableSharedFlow<NavigationAction>(extraBufferCapacity = 1)
    override val navigationActions: SharedFlow<NavigationAction> = _navigationActions.asSharedFlow()

    override suspend fun startScan(): Result<Unit> = barcodeScanner.startScan(context).map { }

    override fun back(result: Any?) {
        _navigationActions.tryEmit(NavigationAction.Back(result))
    }

    override fun openMainScreen() {
        _navigationActions.tryEmit(NavigationAction.Navigate(AppRoute.Main))
    }

    override fun openSetProductScreen(productId: String?) {
        val path =
            if (productId !=
                null
            ) {
                "${AppRoute.SetProduct.path}?${AppRoute.SetProduct.ARG_PRODUCT_ID}=$productId"
            } else {
                AppRoute.SetProduct.path
            }
        _navigationActions.tryEmit(NavigationAction.NavigateWithTemplate(path))
    }
}
