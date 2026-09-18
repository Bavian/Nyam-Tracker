package com.bavian.nyam.tracker.presentation.setproduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetProductScreen(
    state: SetProductUiState,
    onEvent: (SetProductEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.set_product_title),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SetProductEvent.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.set_product_back_description),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(SetProductEvent.ConfirmClicked) },
                        enabled = state.isConfirmEnabled,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.set_product_confirm_description),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                OutlinedTextField(
                    value = state.manufacturer,
                    onValueChange = { onEvent(SetProductEvent.ManufacturerChanged(it)) },
                    label = { Text(stringResource(R.string.set_product_manufacturer)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
            }

            item {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onEvent(SetProductEvent.NameChanged(it)) },
                    label = { Text(stringResource(R.string.set_product_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
            }

            item {
                OutlinedTextField(
                    value = state.calories,
                    onValueChange = { onEvent(SetProductEvent.CaloriesChanged(it)) },
                    label = { Text(stringResource(R.string.set_product_calories)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next,
                        ),
                )
            }

            item {
                OutlinedTextField(
                    value = state.proteins,
                    onValueChange = { onEvent(SetProductEvent.ProteinsChanged(it)) },
                    label = { Text(stringResource(R.string.set_product_proteins)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next,
                        ),
                )
            }

            item {
                OutlinedTextField(
                    value = state.fat,
                    onValueChange = { onEvent(SetProductEvent.FatChanged(it)) },
                    label = { Text(stringResource(R.string.set_product_fat)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next,
                        ),
                )
            }

            item {
                OutlinedTextField(
                    value = state.carbohydrates,
                    onValueChange = { onEvent(SetProductEvent.CarbohydratesChanged(it)) },
                    label = { Text(stringResource(R.string.set_product_carbohydrates)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done,
                        ),
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun SetProductScreenPreview(
    @PreviewParameter(SetProductScreenPreviewParameterProvider::class)
    state: SetProductUiState,
) {
    SetProductScreen(
        state = state,
        onEvent = {},
    )
}

private class SetProductScreenPreviewParameterProvider : PreviewParameterProvider<SetProductUiState> {
    override val values: Sequence<SetProductUiState> =
        sequenceOf(
            SetProductUiState(),
            SetProductUiState(
                manufacturer = "Nestle",
                name = "Nesquik",
                calories = "379",
                proteins = "8.5",
                fat = "1.8",
                carbohydrates = "81.0",
                isConfirmEnabled = true,
            ),
        )
}
