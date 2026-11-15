package com.bavian.nyam.tracker.presentation.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.components.calories.CaloriesSummary
import com.bavian.nyam.tracker.ui.components.diagram.NutrientsDiagram
import com.bavian.nyam.tracker.ui.components.loader.SnakeLoaderWrapper
import com.bavian.nyam.tracker.ui.components.picker.DateRangeCarousel
import com.bavian.nyam.tracker.ui.preview.PreviewScreen
import com.bavian.nyam.tracker.ui.theme.NutrientCarbohydrateColor
import com.bavian.nyam.tracker.ui.theme.NutrientFatColor
import com.bavian.nyam.tracker.ui.theme.NutrientProteinColor
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import java.util.Locale
import kotlin.time.Clock

object StatsScreen {
    @Immutable
    data class DailyStat(
        val dateDays: Long,
        val calories: Float,
        val proteins: Float,
        val fats: Float,
        val carbohydrates: Float,
    )

    sealed interface State {
        data class Loading(
            val startDateDays: Long = defaultStartDateDays(),
            val endDateDays: Long = defaultEndDateDays(),
        ) : State

        data class Success(
            val startDateDays: Long = defaultStartDateDays(),
            val endDateDays: Long = defaultEndDateDays(),
            val dailyStats: ImmutableList<DailyStat> = persistentListOf(),
            val averageCalories: Float = 0f,
            val averageProteins: Float = 0f,
            val averageFats: Float = 0f,
            val averageCarbohydrates: Float = 0f,
        ) : State
    }

    sealed interface Event {
        data class DateRangePicked(
            val startDateDays: Long,
            val endDateDays: Long,
        ) : Event

        data object BackClicked : Event

        data object ScreenStarted : Event
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    state: StatsScreen.State,
    onEvent: (StatsScreen.Event) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    onEvent(StatsScreen.Event.ScreenStarted)
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val successState = state as? StatsScreen.State.Success
    val loading = state is StatsScreen.State.Loading

    SnakeLoaderWrapper(
        isLoading = loading,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.stats_screen_title),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onEvent(StatsScreen.Event.BackClicked) }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_arrow_back),
                                contentDescription = stringResource(R.string.stats_back_description),
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            StatsScreenContent(
                state = successState ?: StatsScreen.State.Success(),
                onEvent = onEvent,
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun StatsScreenContent(
    state: StatsScreen.State.Success,
    onEvent: (StatsScreen.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        DateRangeCarousel(
            state =
                DateRangeCarousel.State(
                    startDateDays = state.startDateDays,
                    endDateDays = state.endDateDays,
                ),
            onEvent = { event ->
                (event as? DateRangeCarousel.Event.DateRangePicked)?.let {
                    onEvent(
                        StatsScreen.Event.DateRangePicked(
                            startDateDays = it.startDateDays,
                            endDateDays = it.endDateDays,
                        ),
                    )
                }
            },
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                CaloriesSummary(
                    state = CaloriesSummary.State(calories = state.averageCalories),
                )
            }

            item {
                NutrientsDiagram(
                    state =
                        NutrientsDiagram.State(
                            proteins = state.averageProteins,
                            fats = state.averageFats,
                            carbohydrates = state.averageCarbohydrates,
                        ),
                )
            }

            item {
                CaloriesChartCard(
                    dailyStats = state.dailyStats,
                )
            }

            item {
                CompactNutrientChartCard(
                    title = stringResource(R.string.nutrients_diagram_proteins),
                    averageValue = state.averageProteins,
                    unit = "g",
                    dailyStats = state.dailyStats,
                    valueSelector = { it.proteins },
                    lineColor = NutrientProteinColor,
                )
            }

            item {
                CompactNutrientChartCard(
                    title = stringResource(R.string.nutrients_diagram_fats),
                    averageValue = state.averageFats,
                    unit = "g",
                    dailyStats = state.dailyStats,
                    valueSelector = { it.fats },
                    lineColor = NutrientFatColor,
                )
            }

            item {
                CompactNutrientChartCard(
                    title = stringResource(R.string.nutrients_diagram_carbohydrates),
                    averageValue = state.averageCarbohydrates,
                    unit = "g",
                    dailyStats = state.dailyStats,
                    valueSelector = { it.carbohydrates },
                    lineColor = NutrientCarbohydrateColor,
                )
            }
        }
    }
}

@Composable
private fun CaloriesChartCard(
    dailyStats: ImmutableList<StatsScreen.DailyStat>,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.nutrients_card_calories),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            val yValues =
                remember(dailyStats) {
                    if (dailyStats.isEmpty()) listOf(0f) else dailyStats.map { it.calories }
                }

            val dateValueFormatter =
                remember(dailyStats) {
                    CartesianValueFormatter { _, value, _ ->
                        val index = value.toInt()
                        if (index in dailyStats.indices) {
                            val date = LocalDate.fromEpochDays(dailyStats[index].dateDays)
                            val monthNum = date.month.ordinal + 1
                            "${date.day}.${monthNum.toString().padStart(2, '0')}"
                        } else {
                            index.toString()
                        }
                    }
                }

            val model =
                remember(yValues) {
                    CartesianChartModel(
                        LineCartesianLayerModel.build {
                            series(y = yValues)
                        },
                    )
                }

            val lineColor = MaterialTheme.colorScheme.primary

            val lineLayer =
                rememberLineCartesianLayer(
                    lineProvider =
                        LineCartesianLayer.LineProvider.series(
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(Fill(lineColor)),
                            ),
                        ),
                )

            CartesianChartHost(
                chart =
                    rememberCartesianChart(
                        lineLayer,
                        startAxis = VerticalAxis.rememberStart(),
                        bottomAxis =
                            HorizontalAxis.rememberBottom(
                                valueFormatter = dateValueFormatter,
                            ),
                    ),
                model = model,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp),
            )
        }
    }
}

@Composable
private fun CompactNutrientChartCard(
    title: String,
    averageValue: Float,
    unit: String,
    dailyStats: ImmutableList<StatsScreen.DailyStat>,
    valueSelector: (StatsScreen.DailyStat) -> Float,
    lineColor: Color,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(lineColor),
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    text =
                        stringResource(
                            R.string.stats_average_format,
                            "${formatValue(averageValue)} $unit",
                        ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            val yValues =
                remember(dailyStats, valueSelector) {
                    if (dailyStats.isEmpty()) listOf(0f) else dailyStats.map(valueSelector)
                }

            val dateValueFormatter =
                remember(dailyStats) {
                    CartesianValueFormatter { _, value, _ ->
                        val index = value.toInt()
                        if (index in dailyStats.indices) {
                            val date = LocalDate.fromEpochDays(dailyStats[index].dateDays)
                            val monthNum = date.month.ordinal + 1
                            "${date.day}.${monthNum.toString().padStart(2, '0')}"
                        } else {
                            index.toString()
                        }
                    }
                }

            val model =
                remember(yValues) {
                    CartesianChartModel(
                        LineCartesianLayerModel.build {
                            series(y = yValues)
                        },
                    )
                }

            val lineLayer =
                rememberLineCartesianLayer(
                    lineProvider =
                        LineCartesianLayer.LineProvider.series(
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(Fill(lineColor)),
                            ),
                        ),
                )

            CartesianChartHost(
                chart =
                    rememberCartesianChart(
                        lineLayer,
                        startAxis = VerticalAxis.rememberStart(),
                        bottomAxis =
                            HorizontalAxis.rememberBottom(
                                valueFormatter = dateValueFormatter,
                            ),
                    ),
                model = model,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(110.dp),
            )
        }
    }
}

private fun defaultStartDateDays(): Long {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val mondayOffset = today.dayOfWeek.ordinal
    return (today - DatePeriod(days = mondayOffset)).toEpochDays()
}

private fun defaultEndDateDays(): Long {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val mondayOffset = today.dayOfWeek.ordinal
    val startOfWeek = today - DatePeriod(days = mondayOffset)
    return (startOfWeek + DatePeriod(days = 6)).toEpochDays()
}

private fun formatValue(value: Float): String =
    if ((value % 1f) == 0f) {
        value.toInt().toString()
    } else {
        String.format(Locale.getDefault(), "%.1f", value)
    }

@Composable
@PreviewScreen
private fun StatsScreenPreview(
    @PreviewParameter(StatsScreenPreviewParameterProvider::class)
    state: StatsScreen.State,
) {
    NyamTrackerTheme {
        StatsScreen(
            state = state,
            onEvent = {},
        )
    }
}

private class StatsScreenPreviewParameterProvider : PreviewParameterProvider<StatsScreen.State> {
    override val values: Sequence<StatsScreen.State>
        get() {
            val start = defaultStartDateDays()
            val end = defaultEndDateDays()
            val sampleDailyStats =
                (0..6)
                    .map { i ->
                        StatsScreen.DailyStat(
                            dateDays = start + i,
                            calories = 1800f + i * 50f,
                            proteins = 80f + i * 2f,
                            fats = 60f + i * 1.5f,
                            carbohydrates = 200f + i * 10f,
                        )
                    }.shuffled()
                    .toImmutableList()

            return sequenceOf(
                StatsScreen.State.Loading(start, end),
                StatsScreen.State.Success(
                    startDateDays = start,
                    endDateDays = end,
                    dailyStats = sampleDailyStats,
                    averageCalories = 1950f,
                    averageProteins = 86f,
                    averageFats = 65f,
                    averageCarbohydrates = 235f,
                ),
            )
        }
}
