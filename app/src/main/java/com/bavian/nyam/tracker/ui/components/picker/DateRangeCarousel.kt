package com.bavian.nyam.tracker.ui.components.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme
import kotlinx.coroutines.flow.debounce
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

object DateRangeCarousel {
    @Immutable
    data class State(
        val startDateDays: Long,
        val endDateDays: Long,
    )

    sealed interface Event {
        data class DateRangePicked(
            val startDateDays: Long,
            val endDateDays: Long,
        ) : Event
    }
}

private const val MILLIS_IN_DAY = 86_400_000L

@OptIn(kotlinx.coroutines.FlowPreview::class)
@Composable
fun DateRangeCarousel(
    state: DateRangeCarousel.State,
    onEvent: (DateRangeCarousel.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val initialPage = Int.MAX_VALUE / 2
    val pagerState =
        rememberPagerState(
            initialPage = initialPage,
        ) { Int.MAX_VALUE }

    var showDatePicker by remember { mutableStateOf(false) }

    val safeStartDays = minOf(state.startDateDays, state.endDateDays)
    val safeEndDays = maxOf(state.startDateDays, state.endDateDays)

    if (showDatePicker) {
        val dateRangePickerState =
            rememberDateRangePickerState(
                initialSelectedStartDateMillis = safeStartDays * MILLIS_IN_DAY,
                initialSelectedEndDateMillis = safeEndDays * MILLIS_IN_DAY,
            )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        val startMillis = dateRangePickerState.selectedStartDateMillis
                        val endMillis = dateRangePickerState.selectedEndDateMillis ?: startMillis
                        if (startMillis != null && endMillis != null) {
                            val startDays = startMillis / MILLIS_IN_DAY
                            val endDays = endMillis / MILLIS_IN_DAY
                            onEvent(
                                DateRangeCarousel.Event.DateRangePicked(
                                    startDateDays = minOf(startDays, endDays),
                                    endDateDays = maxOf(startDays, endDays),
                                ),
                            )
                        }
                        showDatePicker = false
                    },
                    enabled = dateRangePickerState.selectedStartDateMillis != null,
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            },
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                modifier = Modifier.weight(1f),
            )
        }
    }

    val todayDays =
        remember {
            Clock.System.todayIn(TimeZone.currentSystemDefault()).toEpochDays()
        }

    var isProgrammaticScroll by remember { mutableStateOf(false) }

    val daysBetween = safeStartDays - todayDays
    val targetPage = (initialPage.toLong() + daysBetween).toInt()

    // Update pager when state changes from outside
    LaunchedEffect(safeStartDays) {
        if (pagerState.currentPage != targetPage) {
            isProgrammaticScroll = true
            pagerState.animateScrollToPage(targetPage)
            isProgrammaticScroll = false
        }
    }

    // Handle page changes when user swipes
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .debounce(300.milliseconds)
            .collect { page ->
                if (isProgrammaticScroll) {
                    return@collect
                }
                if (page == targetPage) {
                    return@collect
                }
                val pageOffset = (page - initialPage).toLong()
                val newStartDays = todayDays + pageOffset
                val rangeDuration = safeEndDays - safeStartDays
                val newEndDays = newStartDays + rangeDuration
                if (newStartDays != safeStartDays || newEndDays != safeEndDays) {
                    onEvent(DateRangeCarousel.Event.DateRangePicked(newStartDays, newEndDays))
                }
            }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        IconButton(
            onClick = {
                onEvent(
                    DateRangeCarousel.Event.DateRangePicked(
                        startDateDays = safeStartDays - 1,
                        endDateDays = safeEndDays - 1,
                    ),
                )
            },
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_chevron_left),
                contentDescription = stringResource(R.string.calendar_previous_day_description),
            )
        }

        HorizontalPager(
            state = pagerState,
            key = { it },
            modifier = Modifier.weight(1f),
        ) { page ->
            val pageOffset = (page - initialPage).toLong()
            val pageStartDays = todayDays + pageOffset
            val rangeDuration = safeEndDays - safeStartDays
            val pageEndDays = pageStartDays + rangeDuration
            val dateRangeText = formatDateRange(pageStartDays, pageEndDays, todayDays)

            Text(
                text = dateRangeText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            )
        }

        IconButton(
            onClick = {
                onEvent(
                    DateRangeCarousel.Event.DateRangePicked(
                        startDateDays = safeStartDays + 1,
                        endDateDays = safeEndDays + 1,
                    ),
                )
            },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_chevron_right),
                contentDescription = stringResource(R.string.calendar_next_day_description),
            )
        }

        IconButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.padding(end = 16.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_calendar_month),
                contentDescription = stringResource(R.string.calendar_pick_date_description),
            )
        }
    }
}

@Composable
private fun formatDateRange(
    startDays: Long,
    endDays: Long,
    todayDays: Long,
): String {
    val startDate = LocalDate.fromEpochDays(startDays)
    val endDate = LocalDate.fromEpochDays(endDays)
    val todayDate = LocalDate.fromEpochDays(todayDays)

    return if (startDays == endDays) {
        formatSingleDate(startDate, todayDate)
    } else {
        val startStr = formatSingleDate(startDate, todayDate)
        val endStr = formatSingleDate(endDate, todayDate)
        "$startStr – $endStr"
    }
}

@Composable
private fun formatSingleDate(
    date: LocalDate,
    today: LocalDate,
): String =
    when (date) {
        today -> stringResource(R.string.calendar_today)
        today - DatePeriod(days = 1) -> stringResource(R.string.calendar_yesterday)
        today + DatePeriod(days = 1) -> stringResource(R.string.calendar_tomorrow)
        else -> date.toString()
    }

@Composable
@PreviewLightDark
private fun DateRangeCarouselPreview(
    @PreviewParameter(DateRangeCarouselPreviewParameterProvider::class)
    state: DateRangeCarousel.State,
) = NyamTrackerTheme {
    DateRangeCarousel(
        state = state,
        onEvent = {},
    )
}

private class DateRangeCarouselPreviewParameterProvider : PreviewParameterProvider<DateRangeCarousel.State> {
    override val values: Sequence<DateRangeCarousel.State>
        get() {
            val todayDays = Clock.System.todayIn(TimeZone.currentSystemDefault()).toEpochDays()
            return sequenceOf(
                DateRangeCarousel.State(
                    startDateDays = todayDays,
                    endDateDays = todayDays,
                ),
                DateRangeCarousel.State(
                    startDateDays = todayDays - 1,
                    endDateDays = todayDays,
                ),
                DateRangeCarousel.State(
                    startDateDays = todayDays - 7,
                    endDateDays = todayDays,
                ),
            )
        }
}
