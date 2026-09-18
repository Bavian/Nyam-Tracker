package com.bavian.nyam.tracker.di

import com.bavian.nyam.tracker.data.db.ProductDatabase
import com.bavian.nyam.tracker.data.db.ProductDatabaseSqlite
import com.bavian.nyam.tracker.data.mapper.ProductMapper
import com.bavian.nyam.tracker.data.mapper.ProductMapperImpl
import com.bavian.nyam.tracker.data.repository.ProductRepositoryImpl
import com.bavian.nyam.tracker.domain.repository.ProductRepository
import com.bavian.nyam.tracker.domain.usecase.AddProductUseCase
import com.bavian.nyam.tracker.domain.usecase.AddProductUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.GetProductByIdUseCase
import com.bavian.nyam.tracker.domain.usecase.GetProductByIdUseCaseImpl
import com.bavian.nyam.tracker.presentation.main.MainViewModel
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import com.bavian.nyam.tracker.presentation.navigation.AppNavigationImpl
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScanner
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScannerImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule =
    module {
        factoryOf(::BarcodeScannerImpl) bind BarcodeScanner::class
        singleOf(::AppNavigationImpl) bind AppNavigation::class
        viewModelOf(::MainViewModel)

        singleOf(::ProductDatabaseSqlite) bind ProductDatabase::class
        factoryOf(::ProductMapperImpl) bind ProductMapper::class
        factoryOf(::ProductRepositoryImpl) bind ProductRepository::class
        factoryOf(::AddProductUseCaseImpl) bind AddProductUseCase::class
        factoryOf(::GetProductByIdUseCaseImpl) bind GetProductByIdUseCase::class
    }
