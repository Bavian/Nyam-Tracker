package com.bavian.nyam.tracker.di

import androidx.room.Room
import com.bavian.nyam.tracker.data.db.barcode.BarcodeDatabase
import com.bavian.nyam.tracker.data.db.barcode.BarcodeDatabaseImpl
import com.bavian.nyam.tracker.data.db.barcode.BarcodeRoomDatabase
import com.bavian.nyam.tracker.data.db.eatenfood.EatenFoodDatabase
import com.bavian.nyam.tracker.data.db.eatenfood.EatenFoodDatabaseImpl
import com.bavian.nyam.tracker.data.db.eatenfood.EatenFoodRoomDatabase
import com.bavian.nyam.tracker.data.db.product.ProductDatabase
import com.bavian.nyam.tracker.data.db.product.ProductDatabaseImpl
import com.bavian.nyam.tracker.data.db.product.ProductRoomDatabase
import com.bavian.nyam.tracker.data.db.weight.WeightDatabase
import com.bavian.nyam.tracker.data.db.weight.WeightDatabaseImpl
import com.bavian.nyam.tracker.data.db.weight.WeightRoomDatabase
import com.bavian.nyam.tracker.data.mapper.BarcodeMapper
import com.bavian.nyam.tracker.data.mapper.BarcodeMapperImpl
import com.bavian.nyam.tracker.data.mapper.EatenFoodMapper
import com.bavian.nyam.tracker.data.mapper.EatenFoodMapperImpl
import com.bavian.nyam.tracker.data.mapper.ProductMapper
import com.bavian.nyam.tracker.data.mapper.ProductMapperImpl
import com.bavian.nyam.tracker.data.mapper.WeightMapper
import com.bavian.nyam.tracker.data.mapper.WeightMapperImpl
import com.bavian.nyam.tracker.data.repository.BarcodeRepositoryImpl
import com.bavian.nyam.tracker.data.repository.EatenFoodRepositoryImpl
import com.bavian.nyam.tracker.data.repository.ProductRepositoryImpl
import com.bavian.nyam.tracker.data.repository.WeightRepositoryImpl
import com.bavian.nyam.tracker.domain.infrastructure.ProductIdGenerator
import com.bavian.nyam.tracker.domain.infrastructure.ProductIdGeneratorImpl
import com.bavian.nyam.tracker.domain.infrastructure.StringDistanceCalculator
import com.bavian.nyam.tracker.domain.infrastructure.StringDistanceCalculatorDamerauLevenshteinImpl
import com.bavian.nyam.tracker.domain.repository.BarcodeRepository
import com.bavian.nyam.tracker.domain.repository.EatenFoodRepository
import com.bavian.nyam.tracker.domain.repository.ProductRepository
import com.bavian.nyam.tracker.domain.repository.WeightRepository
import com.bavian.nyam.tracker.domain.usecase.barcode.DeleteBarcodeUseCase
import com.bavian.nyam.tracker.domain.usecase.barcode.DeleteBarcodeUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.barcode.GetBarcodeInfoUseCase
import com.bavian.nyam.tracker.domain.usecase.barcode.GetBarcodeInfoUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.barcode.GetBarcodesByProductIdUseCase
import com.bavian.nyam.tracker.domain.usecase.barcode.GetBarcodesByProductIdUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.barcode.SetBarcodeUseCase
import com.bavian.nyam.tracker.domain.usecase.barcode.SetBarcodeUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.eatenfood.AddEatenFoodUseCase
import com.bavian.nyam.tracker.domain.usecase.eatenfood.AddEatenFoodUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.eatenfood.DeleteEatenFoodUseCase
import com.bavian.nyam.tracker.domain.usecase.eatenfood.DeleteEatenFoodUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.eatenfood.GetEatenFoodByIdUseCase
import com.bavian.nyam.tracker.domain.usecase.eatenfood.GetEatenFoodByIdUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.eatenfood.GetEatenFoodUseCase
import com.bavian.nyam.tracker.domain.usecase.eatenfood.GetEatenFoodUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.product.AddProductUseCase
import com.bavian.nyam.tracker.domain.usecase.product.AddProductUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.product.DeleteProductUseCase
import com.bavian.nyam.tracker.domain.usecase.product.DeleteProductUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.product.GetProductByIdUseCase
import com.bavian.nyam.tracker.domain.usecase.product.GetProductByIdUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.product.GetProductsUseCase
import com.bavian.nyam.tracker.domain.usecase.product.GetProductsUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.weight.DeleteWeightUseCase
import com.bavian.nyam.tracker.domain.usecase.weight.DeleteWeightUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.weight.GetNearestWeightUseCase
import com.bavian.nyam.tracker.domain.usecase.weight.GetNearestWeightUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.weight.GetWeightsInRangeUseCase
import com.bavian.nyam.tracker.domain.usecase.weight.GetWeightsInRangeUseCaseImpl
import com.bavian.nyam.tracker.domain.usecase.weight.SetWeightUseCase
import com.bavian.nyam.tracker.domain.usecase.weight.SetWeightUseCaseImpl
import com.bavian.nyam.tracker.presentation.main.MainViewModel
import com.bavian.nyam.tracker.presentation.main.mapper.MainScreenEatenFoodItemMapper
import com.bavian.nyam.tracker.presentation.main.mapper.MainScreenEatenFoodItemMapperImpl
import com.bavian.nyam.tracker.presentation.main.mapper.MainScreenEventMapper
import com.bavian.nyam.tracker.presentation.main.mapper.MainScreenEventMapperImpl
import com.bavian.nyam.tracker.presentation.main.mapper.MainScreenStateMapper
import com.bavian.nyam.tracker.presentation.main.mapper.MainScreenStateMapperImpl
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import com.bavian.nyam.tracker.presentation.navigation.AppNavigationImpl
import com.bavian.nyam.tracker.presentation.productslist.ProductsListViewModel
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListProductMapper
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListProductMapperImpl
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListScreenEventMapper
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListScreenEventMapperImpl
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListScreenStateMapper
import com.bavian.nyam.tracker.presentation.productslist.mapper.ProductsListScreenStateMapperImpl
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScanner
import com.bavian.nyam.tracker.presentation.scanner.BarcodeScannerImpl
import com.bavian.nyam.tracker.presentation.setfood.SetFoodViewModel
import com.bavian.nyam.tracker.presentation.setfood.mapper.SetFoodScreenEventMapper
import com.bavian.nyam.tracker.presentation.setfood.mapper.SetFoodScreenEventMapperImpl
import com.bavian.nyam.tracker.presentation.setfood.mapper.SetFoodScreenStateMapper
import com.bavian.nyam.tracker.presentation.setfood.mapper.SetFoodScreenStateMapperImpl
import com.bavian.nyam.tracker.presentation.setproduct.SetProductViewModel
import com.bavian.nyam.tracker.presentation.setproduct.mapper.SetProductEventMapper
import com.bavian.nyam.tracker.presentation.setproduct.mapper.SetProductEventMapperImpl
import com.bavian.nyam.tracker.presentation.setproduct.mapper.SetProductStateMapper
import com.bavian.nyam.tracker.presentation.setproduct.mapper.SetProductStateMapperImpl
import com.bavian.nyam.tracker.presentation.setproduct.mapper.SetProductUiStateMapper
import com.bavian.nyam.tracker.presentation.setproduct.mapper.SetProductUiStateMapperImpl
import com.bavian.nyam.tracker.presentation.setweight.SetWeightViewModel
import com.bavian.nyam.tracker.presentation.setweight.mapper.SetWeightScreenEventMapper
import com.bavian.nyam.tracker.presentation.setweight.mapper.SetWeightScreenEventMapperImpl
import com.bavian.nyam.tracker.presentation.setweight.mapper.SetWeightScreenStateMapper
import com.bavian.nyam.tracker.presentation.setweight.mapper.SetWeightScreenStateMapperImpl
import com.bavian.nyam.tracker.presentation.util.ErrorHandler
import com.bavian.nyam.tracker.presentation.util.ErrorHandlerImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule =
    module {
        factoryOf(::BarcodeScannerImpl) bind BarcodeScanner::class
        factoryOf(::ErrorHandlerImpl) bind ErrorHandler::class
        singleOf(::AppNavigationImpl) bind AppNavigation::class
        viewModelOf(::MainViewModel)
        viewModelOf(::ProductsListViewModel)
        viewModelOf(::SetProductViewModel)
        viewModel { params ->
            SetFoodViewModel(
                initialStateSource = params.get(),
                getProductByIdUseCase = lazy { get<GetProductByIdUseCase>() },
                getEatenFoodUseCase = lazy { get<GetEatenFoodByIdUseCase>() },
                addEatenFoodUseCase = lazy { get<AddEatenFoodUseCase>() },
                deleteEatenFoodUseCase = lazy { get<DeleteEatenFoodUseCase>() },
                appNavigation = get(),
                productIdGenerator = get(),
            )
        }
        viewModel { params ->
            SetWeightViewModel(
                initialTimestamp = params.getOrNull(),
                getNearestWeightUseCase = lazy { get<GetNearestWeightUseCase>() },
                setWeightUseCase = lazy { get<SetWeightUseCase>() },
                deleteWeightUseCase = lazy { get<DeleteWeightUseCase>() },
                appNavigation = get(),
            )
        }

        single {
            Room
                .databaseBuilder(get(), ProductRoomDatabase::class.java, "products.db")
                .fallbackToDestructiveMigration(true)
                .build()
        }
        single { get<ProductRoomDatabase>().productDao() }

        single {
            Room
                .databaseBuilder(get(), BarcodeRoomDatabase::class.java, "barcodes.db")
                .fallbackToDestructiveMigration(true)
                .build()
        }
        single { get<BarcodeRoomDatabase>().barcodeDao() }

        single {
            Room
                .databaseBuilder(get(), EatenFoodRoomDatabase::class.java, "eaten_food.db")
                .fallbackToDestructiveMigration(true)
                .build()
        }
        single { get<EatenFoodRoomDatabase>().eatenFoodDao() }

        single {
            Room
                .databaseBuilder(get(), WeightRoomDatabase::class.java, "weights.db")
                .fallbackToDestructiveMigration(true)
                .build()
        }
        single { get<WeightRoomDatabase>().weightDao() }
        singleOf(::ProductDatabaseImpl) bind ProductDatabase::class
        singleOf(::BarcodeDatabaseImpl) bind BarcodeDatabase::class
        singleOf(::EatenFoodDatabaseImpl) bind EatenFoodDatabase::class
        singleOf(::WeightDatabaseImpl) bind WeightDatabase::class

        factoryOf(::ProductIdGeneratorImpl) bind ProductIdGenerator::class
        factoryOf(::StringDistanceCalculatorDamerauLevenshteinImpl) bind StringDistanceCalculator::class

        factoryOf(::ProductMapperImpl) bind ProductMapper::class
        factoryOf(::EatenFoodMapperImpl) bind EatenFoodMapper::class
        factoryOf(::MainScreenEatenFoodItemMapperImpl) bind MainScreenEatenFoodItemMapper::class
        factoryOf(::MainScreenStateMapperImpl) bind MainScreenStateMapper::class
        factoryOf(::MainScreenEventMapperImpl) bind MainScreenEventMapper::class
        factoryOf(::SetProductStateMapperImpl) bind SetProductStateMapper::class
        factoryOf(::SetProductEventMapperImpl) bind SetProductEventMapper::class
        factoryOf(::SetProductUiStateMapperImpl) bind SetProductUiStateMapper::class
        factoryOf(::ProductsListProductMapperImpl) bind ProductsListProductMapper::class
        factoryOf(::ProductsListScreenStateMapperImpl) bind ProductsListScreenStateMapper::class
        factoryOf(::ProductsListScreenEventMapperImpl) bind ProductsListScreenEventMapper::class
        factoryOf(::SetFoodScreenEventMapperImpl) bind SetFoodScreenEventMapper::class
        factoryOf(::SetFoodScreenStateMapperImpl) bind SetFoodScreenStateMapper::class
        factoryOf(::SetWeightScreenEventMapperImpl) bind SetWeightScreenEventMapper::class
        factoryOf(::SetWeightScreenStateMapperImpl) bind SetWeightScreenStateMapper::class
        factoryOf(::ProductRepositoryImpl) bind ProductRepository::class
        factoryOf(::EatenFoodRepositoryImpl) bind EatenFoodRepository::class
        factoryOf(::AddProductUseCaseImpl) bind AddProductUseCase::class
        factoryOf(::DeleteProductUseCaseImpl) bind DeleteProductUseCase::class
        factoryOf(::GetProductByIdUseCaseImpl) bind GetProductByIdUseCase::class
        factoryOf(::GetProductsUseCaseImpl) bind GetProductsUseCase::class
        factoryOf(::AddEatenFoodUseCaseImpl) bind AddEatenFoodUseCase::class
        factoryOf(::DeleteEatenFoodUseCaseImpl) bind DeleteEatenFoodUseCase::class
        factoryOf(::GetEatenFoodUseCaseImpl) bind GetEatenFoodUseCase::class
        factoryOf(::GetEatenFoodByIdUseCaseImpl) bind GetEatenFoodByIdUseCase::class

        factoryOf(::SetWeightUseCaseImpl) bind SetWeightUseCase::class
        factoryOf(::DeleteWeightUseCaseImpl) bind DeleteWeightUseCase::class
        factoryOf(::GetWeightsInRangeUseCaseImpl) bind GetWeightsInRangeUseCase::class
        factoryOf(::GetNearestWeightUseCaseImpl) bind GetNearestWeightUseCase::class

        factoryOf(::BarcodeMapperImpl) bind BarcodeMapper::class
        factoryOf(::WeightMapperImpl) bind WeightMapper::class
        factoryOf(::BarcodeRepositoryImpl) bind BarcodeRepository::class
        factoryOf(::WeightRepositoryImpl) bind WeightRepository::class
        factoryOf(::SetBarcodeUseCaseImpl) bind SetBarcodeUseCase::class
        factoryOf(::DeleteBarcodeUseCaseImpl) bind DeleteBarcodeUseCase::class
        factoryOf(::GetBarcodeInfoUseCaseImpl) bind GetBarcodeInfoUseCase::class
        factoryOf(::GetBarcodesByProductIdUseCaseImpl) bind GetBarcodesByProductIdUseCase::class
    }
