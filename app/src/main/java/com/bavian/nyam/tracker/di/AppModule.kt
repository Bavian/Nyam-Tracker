package com.bavian.nyam.tracker.di

import com.bavian.nyam.tracker.presentation.main.MainViewModel
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScanner
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScannerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule =
    module {
        singleOf(::BarcodeScannerImpl) bind BarcodeScanner::class
        viewModelOf(::MainViewModel)
    }
