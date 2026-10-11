package com.bavian.nyam.tracker.presentation.setproduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.base.mapAsImmutableList
import com.bavian.nyam.tracker.ui.components.card.NutrientsCard
import com.bavian.nyam.tracker.ui.components.card.TitledListCard
import com.bavian.nyam.tracker.ui.components.diagram.NutrientsDiagram
import com.bavian.nyam.tracker.ui.model.StringSource
import com.bavian.nyam.tracker.ui.model.isNotEmpty
import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.ui.preview.PreviewScreen
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

object SetProductScreen {
    sealed interface State {
        data class Success(
            val manufacturer: StringSource = StringSource.Empty,
            val name: StringSource = StringSource.Empty,
            val calories: Float = 0f,
            val proteins: Float = 0f,
            val fat: Float = 0f,
            val carbohydrates: Float = 0f,
            val barcodes: ImmutableList<StringSource> = persistentListOf(),
            val barcodeToDelete: StringSource = StringSource.Empty,
            val keyboardOpened: Boolean = false,
        ) : State
    }

    sealed interface Event {
        data class ManufacturerChanged(
            val value: String,
        ) : Event

        data class NameChanged(
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

        data object ConfirmClicked : Event

        data object BackClicked : Event

        data object BarcodesPlusClicked : Event

        data class BarcodeCrossClicked(
            val number: String,
        ) : Event

        data object DeleteBarcodeConfirmed : Event

        data object DeleteBarcodeCancelled : Event

        data object ScreenStarted : Event
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetProductScreen(
    state: SetProductScreen.State,
    onEvent: (SetProductScreen.Event) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    onEvent(SetProductScreen.Event.ScreenStarted)
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    when (state) {
                        is SetProductScreen.State.Success -> {
                            TextField(
                                value = state.name.get(),
                                onValueChange = { onEvent(SetProductScreen.Event.NameChanged(it)) },
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.set_product_name),
                                        style = MaterialTheme.typography.titleLarge,
                                    )
                                },
                                textStyle = MaterialTheme.typography.titleLarge,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                colors =
                                    TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                modifier =
                                    Modifier
                                        .fillMaxWidth(),
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SetProductScreen.Event.BackClicked) }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_arrow_back),
                            contentDescription = stringResource(R.string.set_product_back_description),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(SetProductScreen.Event.ConfirmClicked) },
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_check),
                            contentDescription = stringResource(R.string.set_product_confirm_description),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when (state) {
            is SetProductScreen.State.Success -> {
                SetProductScreenContent(
                    state = state,
                    onEvent = onEvent,
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun SetProductScreenContent(
    state: SetProductScreen.State.Success,
    onEvent: (SetProductScreen.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            OutlinedTextField(
                value = state.manufacturer.get(),
                onValueChange = { onEvent(SetProductScreen.Event.ManufacturerChanged(it)) },
                label = { Text(stringResource(R.string.set_product_manufacturer)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )
        }

        item {
            NutrientsCard(
                state =
                    NutrientsCard.State(
                        kCalories = formatNutrient(state.calories),
                        proteins = formatNutrient(state.proteins),
                        fats = formatNutrient(state.fat),
                        carbohydrates = formatNutrient(state.carbohydrates),
                    ),
                onEvent = { event ->
                    when (event) {
                        is NutrientsCard.Event.KCaloriesChanged ->
                            onEvent(SetProductScreen.Event.CaloriesChanged(event.value))
                        is NutrientsCard.Event.ProteinsChanged ->
                            onEvent(SetProductScreen.Event.ProteinsChanged(event.value))
                        is NutrientsCard.Event.FatsChanged ->
                            onEvent(SetProductScreen.Event.FatChanged(event.value))
                        is NutrientsCard.Event.CarbohydratesChanged ->
                            onEvent(SetProductScreen.Event.CarbohydratesChanged(event.value))
                    }
                },
            )
        }

        item {
            NutrientsDiagram(
                state =
                    NutrientsDiagram.State(
                        proteins = state.proteins,
                        fats = state.fat,
                        carbohydrates = state.carbohydrates,
                    ),
            )
        }

        item {
            TitledListCard(
                state =
                    TitledListCard.State(
                        title = StringSource.Resource(R.string.set_product_barcodes_title),
                        items =
                            state.barcodes.mapAsImmutableList {
                                TitledListCard.Item(
                                    id = it.get(),
                                    text = it,
                                )
                            },
                    ),
                onEvent = { event ->
                    when (event) {
                        TitledListCard.Event.PlusClicked ->
                            onEvent(SetProductScreen.Event.BarcodesPlusClicked)
                        is TitledListCard.Event.ItemCrossClicked ->
                            onEvent(SetProductScreen.Event.BarcodeCrossClicked(event.id))
                    }
                },
            )
        }
    }

    state.barcodeToDelete.takeIf { it.isNotEmpty() }?.let { barcodeNumber ->
        AlertDialog(
            onDismissRequest = { onEvent(SetProductScreen.Event.DeleteBarcodeCancelled) },
            title = { Text(text = stringResource(R.string.set_product_delete_barcode_dialog_title)) },
            text = {
                Text(
                    text = stringResource(R.string.set_product_delete_barcode_dialog_message, barcodeNumber.get()),
                )
            },
            confirmButton = {
                TextButton(onClick = { onEvent(SetProductScreen.Event.DeleteBarcodeConfirmed) }) {
                    Text(text = stringResource(R.string.set_product_delete_barcode_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(SetProductScreen.Event.DeleteBarcodeCancelled) }) {
                    Text(text = stringResource(R.string.set_product_delete_barcode_dialog_cancel))
                }
            },
        )
    }
}

@Composable
@PreviewScreen
private fun SetProductScreenPreview(
    @PreviewParameter(SetProductScreenPreviewParameterProvider::class)
    state: SetProductScreen.State,
) {
    NyamTrackerTheme {
        SetProductScreen(
            state = state,
            onEvent = {},
        )
    }
}

private class SetProductScreenPreviewParameterProvider : PreviewParameterProvider<SetProductScreen.State> {
    override val values: Sequence<SetProductScreen.State> =
        sequenceOf(
            SetProductScreen.State.Success(),
            SetProductScreen.State.Success(
                manufacturer = "Nestle".stringSource,
                name = "Nesquik".stringSource,
                calories = 379f,
                proteins = 8.5f,
                fat = 1.8f,
                carbohydrates = 81.0f,
                barcodes =
                    listOf("4601234567890", "4601234567890", "4609876543210", "4609876543210")
                        .mapAsImmutableList { it.stringSource },
            ),
            SetProductScreen.State.Success(
                manufacturer = "Nestle".stringSource,
                name = "Nesquik".stringSource,
                calories = 379f,
                proteins = 8.5f,
                fat = 1.8f,
                carbohydrates = 81.0f,
                barcodes =
                    listOf("4601234567890", "4601234567890", "4609876543210", "4609876543210")
                        .mapAsImmutableList { it.stringSource },
                barcodeToDelete = "4601234567890".stringSource,
            ),
        )
}

private fun formatNutrient(value: Float): StringSource =
    if (value == 0f) {
        StringSource.Empty
    } else if ((value % 1f) == 0f) {
        value.toInt().toString().stringSource
    } else {
        value.toString().stringSource
    }
