package com.bavian.nyam.tracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object DateCarousel {
    data class State(
        val pickedDate: LocalDate,
    )

    sealed interface Event {
        data class DatePicked(
            val date: LocalDate,
        ) : Event
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun DateCarousel(
    state: DateCarousel.State,
    onEvent: (DateCarousel.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val initialPage = Int.MAX_VALUE / 2
    val pagerState =
        rememberPagerState(
            initialPage = initialPage,
            pageCount = { Int.MAX_VALUE },
        )
    val coroutineScope = rememberCoroutineScope()

    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                    state.pickedDate
                        .atStartOfDayIn(TimeZone.UTC)
                        .toEpochMilliseconds(),
            )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date =
                                Instant
                                    .fromEpochMilliseconds(millis)
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                                    .date
                            onEvent(DateCarousel.Event.DatePicked(date))
                        }
                        showDatePicker = false
                    },
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
            DatePicker(state = datePickerState)
        }
    }

    // Update pager when state change from outside
    LaunchedEffect(state.pickedDate) {
        val daysBetween = (state.pickedDate.toEpochDays() - today.toEpochDays())
        val targetPage = (initialPage.toLong() + daysBetween).toInt()
        if (pagerState.currentPage != targetPage) {
            pagerState.scrollToPage(targetPage)
        }
    }

    // Trigger event when user swipes
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val daysDiff = (page.toLong() - initialPage.toLong())
            val newDate = today.plus(DatePeriod(days = daysDiff.toInt()))
            if (newDate != state.pickedDate) {
                onEvent(DateCarousel.Event.DatePicked(newDate))
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
                coroutineScope.launch {
                    if (pagerState.currentPage > 0) {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
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
            modifier = Modifier.weight(1f),
        ) { page ->
            val daysDiff = (page.toLong() - initialPage.toLong())
            val date = today.plus(DatePeriod(days = daysDiff.toInt()))
            val dateText =
                when (date) {
                    today -> stringResource(R.string.calendar_today)
                    today.minus(DatePeriod(days = 1)) -> stringResource(R.string.calendar_yesterday)
                    today.plus(DatePeriod(days = 1)) -> stringResource(R.string.calendar_tomorrow)
                    else -> date.toString()
                }

            Text(
                text = dateText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            )
        }

        IconButton(
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage < Int.MAX_VALUE - 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
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
@PreviewLightDark
private fun DateCarouselPreview(
    @PreviewParameter(DateCarouselPreviewParameterProvider::class)
    state: DateCarousel.State,
) {
    DateCarousel(
        state = state,
        onEvent = {},
    )
}

private class DateCarouselPreviewParameterProvider : PreviewParameterProvider<DateCarousel.State> {
    @OptIn(ExperimentalTime::class)
    override val values: Sequence<DateCarousel.State> =
        sequenceOf(
            DateCarousel.State(
                pickedDate = LocalDate(2023, 1, 1),
            ),
            DateCarousel.State(
                pickedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            ),
            DateCarousel.State(
                pickedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()) - DatePeriod(days = 1),
            ),
            DateCarousel.State(
                pickedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()) + DatePeriod(days = 1),
            ),
        )
}
