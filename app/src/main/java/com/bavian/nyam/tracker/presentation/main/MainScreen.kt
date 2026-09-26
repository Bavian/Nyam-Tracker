package com.bavian.nyam.tracker.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.components.picker.DateCarousel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

object MainScreen {
    sealed interface State {
        data class Success(
            val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            val eatenFoodGroups: ImmutableList<EatenFoodGroup> = persistentListOf(),
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
    }
}

@Composable
fun MainScreen(
    state: MainScreen.State,
    onEvent: (MainScreen.Event) -> Unit,
) {
    when (state) {
        is MainScreen.State.Success -> MainScreenSuccess(state, onEvent)
    }
}

@Composable
fun MainScreenSuccess(
    state: MainScreen.State.Success,
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
                DateCarousel(
                    state = DateCarousel.State(state.selectedDate),
                    onEvent = { event ->
                        when (event) {
                            is DateCarousel.Event.DatePicked -> {
                                onEvent(MainScreen.Event.CalendarDatePicked(event.date))
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
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
            items(
                items = state.eatenFoodGroups,
                key = { group -> group.title },
            ) { group ->
                EatenFoodGroupBlock(
                    group = group,
                    onItemClick = { foodId ->
                        onEvent(MainScreen.Event.EatenFoodTap(foodId))
                    },
                )
            }
        }
    }
}

@Composable
private fun EatenFoodGroupBlock(
    group: MainScreen.State.Success.EatenFoodGroup,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = group.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
            )

            group.items.forEachIndexed { index, item ->
                if (index > 0) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                    )
                }
                EatenFoodItemView(
                    item = item,
                    onClick = { onItemClick(item.id) },
                )
            }
        }
    }
}

@Composable
private fun EatenFoodItemView(
    item: MainScreen.State.Success.EatenFoodGroup.Item,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            if (item.time.isNotEmpty()) {
                Text(
                    text = item.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        if (item.manufacturer.isNotEmpty()) {
            Text(
                text = item.manufacturer,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Row(
            modifier = Modifier.padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            EatenFoodNutrientInfo(
                label = stringResource(R.string.eaten_food_weight_prefix),
                value = item.weight,
            )
            EatenFoodNutrientInfo(
                label = stringResource(R.string.products_list_calories_prefix),
                value = item.calories,
            )
            EatenFoodNutrientInfo(
                label = stringResource(R.string.products_list_proteins_prefix),
                value = item.proteins,
            )
            EatenFoodNutrientInfo(
                label = stringResource(R.string.products_list_fat_prefix),
                value = item.fat,
            )
            EatenFoodNutrientInfo(
                label = stringResource(R.string.products_list_carbohydrates_prefix),
                value = item.carbohydrates,
            )
        }
    }
}

@Composable
private fun EatenFoodNutrientInfo(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun MainScreenPreview(
    @PreviewParameter(MainScreenPreviewParameterProvider::class)
    state: MainScreen.State.Success,
) {
    MainScreen(
        state = state,
        onEvent = {},
    )
}

private class MainScreenPreviewParameterProvider : PreviewParameterProvider<MainScreen.State> {
    override val values: Sequence<MainScreen.State> =
        sequenceOf(
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
            ),
        )
}
