package com.bavian.nyam.tracker.di

import com.bavian.nyam.tracker.data.db.AppDatabaseHelper
import com.bavian.nyam.tracker.data.db.BarcodeDatabase
import com.bavian.nyam.tracker.data.db.BarcodeDatabaseImpl
import com.bavian.nyam.tracker.data.db.ProductDatabase
import com.bavian.nyam.tracker.data.db.ProductDatabaseImpl
import com.bavian.nyam.tracker.data.mapper.BarcodeMapper
import com.bavian.nyam.tracker.data.mapper.BarcodeMapperImpl
import com.bavian.nyam.tracker.data.mapper.ProductMapper
import com.bavian.nyam.tracker.data.mapper.ProductMapperImpl
import com.bavian.nyam.tracker.data.repository.BarcodeRepositoryImpl
import com.bavian.nyam.tracker.data.repository.ProductRepositoryImpl
import com.bavian.nyam.tracker.domain.infrastructure.ProductIdGenerator
import com.bavian.nyam.tracker.domain.infrastructure.ProductIdGeneratorImpl
import com.bavian.nyam.tracker.domain.repository.BarcodeRepository
import com.bavian.nyam.tracker.domain.repository.ProductRepository
import com.bavian.nyam.tracker.domain.usecase.AddProductUseCase
import com.bavian.nyam.tracker.domain.usecase.AddProductUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.GetBarcodeInfoUseCase
import com.bavian.nyam.tracker.domain.usecase.GetBarcodeInfoUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.GetProductByIdUseCase
import com.bavian.nyam.tracker.domain.usecase.GetProductByIdUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.SetBarcodeUseCase
import com.bavian.nyam.tracker.domain.usecase.SetBarcodeUseCaseImpl
import com.bavian.nyam.tracker.presentation.main.MainViewModel
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import com.bavian.nyam.tracker.presentation.navigation.AppNavigationImpl
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScanner
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScannerImpl
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModel
import com.bavian.nyam.tracker.presentation.setproduct.mapper.SetProductUiStateMapper
import com.bavian.nyam.tracker.presentation.setproduct.mapper.SetProductUiStateMapperImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule =
    module {
        factoryOf(::BarcodeScannerImpl) bind BarcodeScanner::class
        singleOf(::AppNavigationImpl) bind AppNavigation::class
        viewModelOf(::MainViewModel)

        viewModel { parameters ->
            SetProductViewModel(
                initialProductId = parameters.getOrNull(),
                addProductUseCase = get(),
                getProductByIdUseCase = get(),
                productMapper = get(),
                appNavigation = get(),
            )
        }

        singleOf(::AppDatabaseHelper)
        singleOf(::ProductDatabaseImpl) bind ProductDatabase::class
        singleOf(::BarcodeDatabaseImpl) bind BarcodeDatabase::class

        factoryOf(::ProductIdGeneratorImpl) bind ProductIdGenerator::class

        factoryOf(::ProductMapperImpl) bind ProductMapper::class
        factoryOf(::SetProductUiStateMapperImpl) bind SetProductUiStateMapper::class
        factoryOf(::ProductRepositoryImpl) bind ProductRepository::class
        factoryOf(::AddProductUseCaseImpl) bind AddProductUseCase::class
        factoryOf(::GetProductByIdUseCaseImpl) bind GetProductByIdUseCase::class

        factoryOf(::BarcodeMapperImpl) bind BarcodeMapper::class
        factoryOf(::BarcodeRepositoryImpl) bind BarcodeRepository::class
        factoryOf(::SetBarcodeUseCaseImpl) bind SetBarcodeUseCase::class
        factoryOf(::GetBarcodeInfoUseCaseImpl) bind GetBarcodeInfoUseCase::class
    }
