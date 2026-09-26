package com.bavian.nyam.tracker.presentation.main.mapper

import com.bavian.nyam.tracker.presentation.main.MainScreen
import com.bavian.nyam.tracker.presentation.main.MainScreenViewModelEvent

class MainScreenEventMapperImpl : MainScreenEventMapper {
    override fun map(event: MainScreen.Event): MainScreenViewModelEvent =
        when (event) {
            is MainScreen.Event.StartScanTap -> MainScreenViewModelEvent.StartScanTap
            is MainScreen.Event.ProductsListTap -> MainScreenViewModelEvent.ProductsListTap
            is MainScreen.Event.CalendarDatePicked -> MainScreenViewModelEvent.CalendarDatePicked(event.date)
            is MainScreen.Event.ScreenStarted -> MainScreenViewModelEvent.ScreenViewModelStarted
            is MainScreen.Event.EatenFoodTap -> MainScreenViewModelEvent.EatenFoodTap(event.foodId)
        }
}
