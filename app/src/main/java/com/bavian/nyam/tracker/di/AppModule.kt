package com.bavian.nyam.tracker.di

import com.bavian.nyam.tracker.BakingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::BakingViewModel)
}
