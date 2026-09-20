package com.bavian.nyam.tracker.ui.components.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalTime

object TimePicker {
    data class State(
        val time: LocalTime,
    )

    sealed interface Event {
        data class TimeChanged(
            val time: LocalTime,
        ) : Event
    }
}

@Composable
fun TimePicker(
    state: TimePicker.State,
    onEvent: (TimePicker.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hour = state.time.hour
    val minute = state.time.minute

    var h1 by remember(hour) { mutableIntStateOf(hour / 10) }
    var h2 by remember(hour) { mutableIntStateOf(hour % 10) }
    var m1 by remember(minute) { mutableIntStateOf(minute / 10) }
    var m2 by remember(minute) { mutableIntStateOf(minute % 10) }
    var h2Range by remember(h1) { mutableStateOf(getH2Range(h1)) }

    LaunchedEffect(h2Range) {
        h2 = h2.coerceIn(h2Range)
    }

    LaunchedEffect(h1, h2, m1, m2) {
        onEvent(TimePicker.Event.TimeChanged(LocalTime(h1 * 10 + h2, m1 * 10 + m2)))
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Hour 1: 0, 1, 2
        NumberPickerRoulette(
            state =
                NumberPickerRoulette.State(
                    list = persistentListOf(0, 1, 2),
                    value = h1,
                ),
            onEvent = { event ->
                if (event is NumberPickerRoulette.Event.ValueChanged) {
                    h1 = event.value
                    h2Range = getH2Range(h1)
                    h2 = h2.coerceIn(h2Range)
                }
            },
            modifier = Modifier.width(64.dp),
        )

        NumberPickerRoulette(
            state =
                NumberPickerRoulette.State(
                    list = h2Range.toImmutableList(),
                    value = h2,
                ),
            onEvent = { event ->
                if (event is NumberPickerRoulette.Event.ValueChanged) {
                    h2 = event.value
                }
            },
            modifier = Modifier.width(64.dp),
        )

        Text(
            text = ":",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 4.dp),
        )

        // Minute 1: 0..5
        NumberPickerRoulette(
            state =
                NumberPickerRoulette.State(
                    list = (0..5).toImmutableList(),
                    value = m1,
                ),
            onEvent = { event ->
                if (event is NumberPickerRoulette.Event.ValueChanged) {
                    m1 = event.value
                }
            },
            modifier = Modifier.width(64.dp),
        )

        // Minute 2: 0..9
        NumberPickerRoulette(
            state =
                NumberPickerRoulette.State(
                    list = (0..9).toImmutableList(),
                    value = m2,
                ),
            onEvent = { event ->
                if (event is NumberPickerRoulette.Event.ValueChanged) {
                    m2 = event.value
                }
            },
            modifier = Modifier.width(64.dp),
        )
    }
}

private fun getH2Range(h1: Int) = if (h1 == 2) 0..3 else 0..9

@Preview(showBackground = true)
@Composable
private fun TimePickerPreview(
    @PreviewParameter(TimePickerPreviewParameterProvider::class)
    state: TimePicker.State,
) {
    var rememberedState by remember { mutableStateOf(state) }

    TimePicker(
        state = rememberedState,
        onEvent = { event ->
            when (event) {
                is TimePicker.Event.TimeChanged -> {
                    rememberedState =
                        rememberedState.copy(
                            time = event.time,
                        )
                }
            }
        },
        modifier = Modifier.padding(16.dp),
    )
}

private class TimePickerPreviewParameterProvider : PreviewParameterProvider<TimePicker.State> {
    override val values: Sequence<TimePicker.State> =
        sequenceOf(
            TimePicker.State(LocalTime(12, 0)),
            TimePicker.State(LocalTime(23, 59)),
            TimePicker.State(LocalTime(0, 0)),
            TimePicker.State(LocalTime(9, 45)),
        )
}
