package com.bavian.nyam.tracker.presentation.setweight.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.components.loader.SnakeLoaderWrapper
import com.bavian.nyam.tracker.ui.components.picker.DateCarousel
import com.bavian.nyam.tracker.ui.components.picker.TimePicker
import com.bavian.nyam.tracker.ui.model.StringSource
import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.ui.preview.PreviewScreen
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

object SetWeightScreen {
    sealed interface State {
        val canDelete: Boolean
        val deleteConfirmation: Boolean

        data class Loading(
            override val canDelete: Boolean = false,
            override val deleteConfirmation: Boolean = false,
        ) : State

        data class Success(
            val weight: StringSource = StringSource.Empty,
            val date: LocalDate,
            val time: LocalTime,
            override val canDelete: Boolean = false,
            override val deleteConfirmation: Boolean = false,
        ) : State
    }

    sealed interface Event {
        data class WeightChanged(
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

        data object DeleteClicked : Event

        data object DeleteConfirmed : Event

        data object DeleteCancelled : Event
    }
}

@Composable
fun SetWeightScreen(
    state: SetWeightScreen.State,
    onEvent: (SetWeightScreen.Event) -> Unit,
) {
    val successState = state as? SetWeightScreen.State.Success
    SetWeightScreenContent(
        weight = successState?.weight ?: StringSource.Empty,
        date = successState?.date ?: Clock.System.todayIn(TimeZone.currentSystemDefault()),
        time =
            successState?.time ?: Clock.System
                .now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .time,
        canDelete = state.canDelete,
        deleteConfirmation = state.deleteConfirmation,
        loading = state is SetWeightScreen.State.Loading,
        onEvent = onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetWeightScreenContent(
    weight: StringSource,
    date: LocalDate,
    time: LocalTime,
    canDelete: Boolean,
    deleteConfirmation: Boolean,
    loading: Boolean,
    onEvent: (SetWeightScreen.Event) -> Unit,
) {
    SnakeLoaderWrapper(
        isLoading = loading,
    ) {
        Scaffold(
            modifier = Modifier.imePadding(),
            topBar = {
                TopAppBar(
                    title = {
                        TextField(
                            value = weight.get(),
                            onValueChange = { onEvent(SetWeightScreen.Event.WeightChanged(it)) },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.set_weight_hint),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            },
                            textStyle = MaterialTheme.typography.titleLarge,
                            singleLine = true,
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done,
                                ),
                            colors =
                                TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onEvent(SetWeightScreen.Event.BackClicked) }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_arrow_back),
                                contentDescription = stringResource(R.string.set_product_back_description),
                            )
                        }
                    },
                    actions = {
                        if (canDelete) {
                            IconButton(onClick = { onEvent(SetWeightScreen.Event.DeleteClicked) }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_delete_forever),
                                    contentDescription = stringResource(R.string.products_list_delete),
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.width(48.dp))
                        }
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onEvent(SetWeightScreen.Event.SavePressed) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_check),
                        contentDescription = stringResource(R.string.set_weight_save_content_description),
                    )
                }
            },
        ) { paddingValues ->
            if (deleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { onEvent(SetWeightScreen.Event.DeleteCancelled) },
                    title = { Text(text = stringResource(R.string.set_weight_delete_dialog_title)) },
                    text = { Text(text = stringResource(R.string.set_weight_delete_dialog_message)) },
                    confirmButton = {
                        TextButton(onClick = { onEvent(SetWeightScreen.Event.DeleteConfirmed) }) {
                            Text(
                                text = stringResource(R.string.set_weight_delete_dialog_confirm),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { onEvent(SetWeightScreen.Event.DeleteCancelled) }) {
                            Text(text = stringResource(R.string.set_weight_delete_dialog_cancel))
                        }
                    },
                )
            }

            Column(
                modifier =
                    Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                DateCarousel(
                    state = DateCarousel.State(date),
                    onEvent = { event ->
                        if (event is DateCarousel.Event.DatePicked) {
                            onEvent(SetWeightScreen.Event.DateChanged(event.date))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                ) {
                    TimePicker(
                        state = TimePicker.State(time),
                        onEvent = { event ->
                            if (event is TimePicker.Event.TimeChanged) {
                                onEvent(SetWeightScreen.Event.TimeChanged(event.time))
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
@PreviewScreen
private fun SetWeightScreenPreview(
    @PreviewParameter(SetWeightScreenPreviewParameterProvider::class)
    state: SetWeightScreen.State,
) {
    SetWeightScreen(
        state = state,
        onEvent = {},
    )
}

internal class SetWeightScreenPreviewParameterProvider : PreviewParameterProvider<SetWeightScreen.State> {
    override val values: Sequence<SetWeightScreen.State> =
        sequenceOf(
            SetWeightScreen.State.Loading(
                canDelete = true,
            ),
            SetWeightScreen.State.Success(
                weight = "72.5".stringSource,
                date = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                time =
                    Clock.System
                        .now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .time,
                canDelete = true,
            ),
            SetWeightScreen.State.Success(
                weight = "72.5".stringSource,
                date = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                time =
                    Clock.System
                        .now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .time,
                canDelete = true,
                deleteConfirmation = true,
            ),
        )
}
