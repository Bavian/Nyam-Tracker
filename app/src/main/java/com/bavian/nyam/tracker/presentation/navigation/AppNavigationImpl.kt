package com.bavian.nyam.tracker.presentation.navigation

import android.content.Context
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation.NavigationAction
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScanner
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppNavigationImpl(
    private val barcodeScanner: BarcodeScanner,
) : AppNavigation {
    private val _navigationActions = MutableSharedFlow<NavigationAction>(extraBufferCapacity = 1)
    override val navigationActions: SharedFlow<NavigationAction> = _navigationActions.asSharedFlow()

    override fun startScan(context: Context) {
        barcodeScanner.startScan(context)
    }

    override fun back() {
        _navigationActions.tryEmit(NavigationAction.Back)
    }

    override fun openMainScreen() {
        _navigationActions.tryEmit(NavigationAction.Navigate(AppRoute.Main))
    }
}
