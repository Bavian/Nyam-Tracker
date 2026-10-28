package com.bavian.nyam.tracker.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.base.mapAsImmutableList
import com.bavian.nyam.tracker.ui.components.calories.CaloriesSummary
import com.bavian.nyam.tracker.ui.components.card.FoodListCard
import com.bavian.nyam.tracker.ui.components.diagram.NutrientsDiagram
import com.bavian.nyam.tracker.ui.components.loader.SnakeLoaderWrapper
import com.bavian.nyam.tracker.ui.components.picker.DateCarousel
import com.bavian.nyam.tracker.ui.model.StringSource
import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.ui.preview.PreviewScreen
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

object MainScreen {
    sealed interface State {
        val selectedDate: LocalDate
        val weight: StringSource

        data class Loading(
            override val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            override val weight: StringSource = "-- kg".stringSource,
        ) : State

        data class Success(
            override val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            override val weight: StringSource = "-- kg".stringSource,
            val eatenFoodGroups: ImmutableList<EatenFoodGroup> = persistentListOf(),
            val kCalories: Float = 0f,
            val proteins: Float = 0f,
            val fat: Float = 0f,
            val carbohydrates: Float = 0f,
        ) : State {
            data class EatenFoodGroup(
                val title: String,
                val items: ImmutableList<Item>,
            ) {
                data class Item(
                    val id: String,
                    val name: String,
                    val manufacturer: String,
                    val weight: String,
                    val calories: String,
                    val proteins: String,
                    val fat: String,
                    val carbohydrates: String,
                    val time: String,
                )
            }
        }
    }

    sealed interface Event {
        data object StartScanTap : Event

        data object ProductsListTap : Event

        data class CalendarDatePicked(
            val date: LocalDate,
        ) : Event

        data object ScreenStarted : Event

        data class EatenFoodTap(
            val foodId: String,
        ) : Event

        data class EatenFoodEditTap(
            val foodId: String,
        ) : Event

        data class EatenFoodDeleteTap(
            val foodId: String,
        ) : Event

        data object WeightTap : Event
    }
}

@Composable
fun MainScreen(
    state: MainScreen.State,
    onEvent: (MainScreen.Event) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    onEvent(MainScreen.Event.ScreenStarted)
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val successState = state as? MainScreen.State.Success
    MainScreenContent(
        selectedDate = state.selectedDate,
        weight = state.weight,
        eatenFoodGroups = successState?.eatenFoodGroups ?: persistentListOf(),
        kCalories = successState?.kCalories ?: 0f,
        proteins = successState?.proteins ?: 0f,
        fat = successState?.fat ?: 0f,
        carbohydrates = successState?.carbohydrates ?: 0f,
        loading = state is MainScreen.State.Loading,
        onEvent = onEvent,
    )
}

@Composable
fun MainScreenContent(
    selectedDate: LocalDate,
    weight: StringSource,
    eatenFoodGroups: ImmutableList<MainScreen.State.Success.EatenFoodGroup>,
    kCalories: Float,
    proteins: Float,
    fat: Float,
    carbohydrates: Float,
    loading: Boolean,
    onEvent: (MainScreen.Event) -> Unit,
) {
    SnakeLoaderWrapper(
        isLoading = loading,
    ) {
        Scaffold(
            topBar = {
                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background,
                    shadowElevation = 2.dp,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = weight.get(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier =
                                Modifier
                                    .padding(start = 16.dp, top = 8.dp)
                                    .clickable { onEvent(MainScreen.Event.WeightTap) },
                        )
                        DateCarousel(
                            state = DateCarousel.State(selectedDate),
                            onEvent = { event ->
                                when (event) {
                                    is DateCarousel.Event.DatePicked -> {
                                        onEvent(MainScreen.Event.CalendarDatePicked(event.date))
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        CaloriesSummary(
                            calories = kCalories,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        HorizontalDivider()
                    }
                }
            },
            floatingActionButton = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    FloatingActionButton(
                        onClick = { onEvent(MainScreen.Event.ProductsListTap) },
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_add_2),
                            contentDescription = stringResource(R.string.main_products_list),
                        )
                    }

                    FloatingActionButton(
                        onClick = { onEvent(MainScreen.Event.StartScanTap) },
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_barcode_scanner),
                            contentDescription = stringResource(R.string.main_scan_barcode),
                        )
                    }
                }
            },
        ) { innerPadding ->
            LazyColumn(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item(key = "nutrients_diagram") {
                    NutrientsDiagram(
                        state =
                            NutrientsDiagram.State(
                                proteins = proteins,
                                fats = fat,
                                carbohydrates = carbohydrates,
                            ),
                    )
                }

                items(
                    items = eatenFoodGroups,
                    key = { group -> group.title },
                ) { group ->
                    FoodListCard(
                        state =
                            FoodListCard.State(
                                title = group.title.stringSource,
                                items = group.items.mapAsImmutableList(::mapFoodItem),
                            ),
                        onEvent = { event ->
                            when (event) {
                                is FoodListCard.Event.ItemClicked -> {
                                    onEvent(MainScreen.Event.EatenFoodTap(event.id))
                                }
                                is FoodListCard.Event.EditClicked -> {
                                    onEvent(MainScreen.Event.EatenFoodEditTap(event.id))
                                }
                                is FoodListCard.Event.DeleteClicked -> {
                                    onEvent(MainScreen.Event.EatenFoodDeleteTap(event.id))
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

private fun mapFoodItem(item: MainScreen.State.Success.EatenFoodGroup.Item): FoodListCard.Item =
    FoodListCard.Item(
        id = item.id,
        name = item.name,
        manufacturer = item.manufacturer,
        weight = item.weight,
        calories = item.calories,
        proteins = item.proteins,
        fat = item.fat,
        carbohydrates = item.carbohydrates,
        time = item.time,
    )

@Composable
@PreviewScreen
private fun MainScreenPreview(
    @PreviewParameter(MainScreenPreviewParameterProvider::class)
    state: MainScreen.State,
) {
    NyamTrackerTheme {
        MainScreen(
            state = state,
            onEvent = {},
        )
    }
}

private class MainScreenPreviewParameterProvider : PreviewParameterProvider<MainScreen.State> {
    override val values: Sequence<MainScreen.State> =
        sequenceOf(
            MainScreen.State.Loading(),
            MainScreen.State.Success(),
            MainScreen.State.Success(
                eatenFoodGroups =
                    persistentListOf(
                        MainScreen.State.Success.EatenFoodGroup(
                            title = "08:00 - 09:00",
                            items =
                                persistentListOf(
                                    MainScreen.State.Success.EatenFoodGroup.Item(
                                        id = "1",
                                        name = "Oatmeal",
                                        manufacturer = "Quaker",
                                        weight = "150",
                                        calories = "220",
                                        proteins = "8",
                                        fat = "4",
                                        carbohydrates = "38",
                                        time = "08:15",
                                    ),
                                ),
                        ),
                        MainScreen.State.Success.EatenFoodGroup(
                            title = "18:00 - 19:00",
                            items =
                                persistentListOf(
                                    MainScreen.State.Success.EatenFoodGroup.Item(
                                        id = "2",
                                        name = "Nesquik",
                                        manufacturer = "Nestle",
                                        weight = "200",
                                        calories = "379",
                                        proteins = "8.5",
                                        fat = "1.8",
                                        carbohydrates = "81",
                                        time = "18:30",
                                    ),
                                    MainScreen.State.Success.EatenFoodGroup.Item(
                                        id = "3",
                                        name = "Banana",
                                        manufacturer = "Chiquita",
                                        weight = "120",
                                        calories = "105",
                                        proteins = "1.3",
                                        fat = "0.3",
                                        carbohydrates = "27",
                                        time = "18:45",
                                    ),
                                ),
                        ),
                    ),
                kCalories = 704f,
                proteins = 17.8f,
                fat = 6.1f,
                carbohydrates = 146f,
            ),
        )
}
