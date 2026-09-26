package com.bavian.nyam.tracker.presentation.setfood.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.components.picker.DateCarousel
import com.bavian.nyam.tracker.ui.components.picker.TimePicker
import com.bavian.nyam.tracker.ui.model.StringSource
import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.ui.preview.PreviewScreen
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

object SetFoodScreen {
    sealed interface State {
        data class Success(
            val title: StringSource,
            val manufacturer: StringSource,
            val name: StringSource,
            val weight: StringSource,
            val kCalories: StringSource,
            val proteins: StringSource,
            val fat: StringSource,
            val carbohydrates: StringSource,
            val date: LocalDate,
            val time: LocalTime,
        ) : State
    }

    sealed interface Event {
        data class ManufacturerChanged(
            val value: String,
        ) : Event

        data class NameChanged(
            val value: String,
        ) : Event

        data class WeightChanged(
            val value: String,
        ) : Event

        data class CaloriesChanged(
            val value: String,
        ) : Event

        data class ProteinsChanged(
            val value: String,
        ) : Event

        data class FatChanged(
            val value: String,
        ) : Event

        data class CarbohydratesChanged(
            val value: String,
        ) : Event

        data class DateChanged(
            val value: LocalDate,
        ) : Event

        data class TimeChanged(
            val value: LocalTime,
        ) : Event

        data object SavePressed : Event

        data object BackClicked : Event
    }
}

@Composable
fun SetFoodScreen(
    state: SetFoodScreen.State,
    onEvent: (SetFoodScreen.Event) -> Unit,
) {
    when (state) {
        is SetFoodScreen.State.Success -> SetFoodScreenSuccess(state, onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetFoodScreenSuccess(
    state: SetFoodScreen.State.Success,
    onEvent: (SetFoodScreen.Event) -> Unit,
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = state.title.get(),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SetFoodScreen.Event.BackClicked) }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_arrow_back),
                            contentDescription = stringResource(R.string.set_product_back_description),
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.height(48.dp))
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(SetFoodScreen.Event.SavePressed) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_check),
                    contentDescription = stringResource(R.string.set_food_save_content_description),
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        bottom = 16.dp,
                        start = 24.dp,
                        end = 24.dp,
                    ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DateCarousel(
                state = DateCarousel.State(state.date),
                onEvent = { event ->
                    if (event is DateCarousel.Event.DatePicked) {
                        onEvent(SetFoodScreen.Event.DateChanged(event.date))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
            ) {
                TimePicker(
                    state = TimePicker.State(state.time),
                    onEvent = {
                        if (it is TimePicker.Event.TimeChanged) {
                            onEvent(SetFoodScreen.Event.TimeChanged(it.time))
                        }
                    },
                )
            }

            OutlinedTextField(
                value = state.manufacturer.get(),
                onValueChange = { onEvent(SetFoodScreen.Event.ManufacturerChanged(it)) },
                label = { Text(stringResource(R.string.set_food_manufacturer)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            OutlinedTextField(
                value = state.name.get(),
                onValueChange = { onEvent(SetFoodScreen.Event.NameChanged(it)) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            OutlinedTextField(
                value = state.weight.get(),
                onValueChange = { onEvent(SetFoodScreen.Event.WeightChanged(it)) },
                label = { Text("Weight (g)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            OutlinedTextField(
                value = state.kCalories.get(),
                onValueChange = { onEvent(SetFoodScreen.Event.CaloriesChanged(it)) },
                label = { Text("Calories (kcal)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            OutlinedTextField(
                value = state.proteins.get(),
                onValueChange = { onEvent(SetFoodScreen.Event.ProteinsChanged(it)) },
                label = { Text("Proteins (g)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            OutlinedTextField(
                value = state.fat.get(),
                onValueChange = { onEvent(SetFoodScreen.Event.FatChanged(it)) },
                label = { Text("Fat (g)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            OutlinedTextField(
                value = state.carbohydrates.get(),
                onValueChange = { onEvent(SetFoodScreen.Event.CarbohydratesChanged(it)) },
                label = { Text("Carbohydrates (g)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
@PreviewScreen
private fun SetFoodScreenPreview(
    @PreviewParameter(SetFoodScreenPreviewParameterProvider::class)
    state: SetFoodScreen.State,
) {
    SetFoodScreen(
        state = state,
        onEvent = {},
    )
}

internal class SetFoodScreenPreviewParameterProvider : PreviewParameterProvider<SetFoodScreen.State> {
    override val values: Sequence<SetFoodScreen.State> =
        sequenceOf(
            SetFoodScreen.State.Success(
                title = StringSource.Resource(R.string.set_food_title_future),
                manufacturer = "Sample Manufacturer".stringSource,
                name = "Sample Food".stringSource,
                weight = "250".stringSource,
                kCalories = "500".stringSource,
                proteins = "20".stringSource,
                fat = "10".stringSource,
                carbohydrates = "40".stringSource,
                date = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                time =
                    Clock.System
                        .now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .time,
            ),
            SetFoodScreen.State.Success(
                title = StringSource.Resource(R.string.set_food_title_past),
                manufacturer = StringSource.Empty,
                name = StringSource.Empty,
                weight = StringSource.Empty,
                kCalories = StringSource.Empty,
                proteins = StringSource.Empty,
                fat = StringSource.Empty,
                carbohydrates = StringSource.Empty,
                date = Clock.System.todayIn(TimeZone.currentSystemDefault()) - DatePeriod(days = 1),
                time =
                    Clock.System
                        .now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .time,
            ),
        )
}
