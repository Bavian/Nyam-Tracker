package com.bavian.nyam.tracker.di

import com.bavian.nyam.tracker.BakingViewModel
import com.bavian.nyam.tracker.BarcodeScanner
import com.bavian.nyam.tracker.BarcodeScannerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::BarcodeScannerImpl) bind BarcodeScanner::class
    viewModelOf(::BakingViewModel)
}
