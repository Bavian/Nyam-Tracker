package com.bavian.nyam.tracker.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.model.StringSource
import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme

object NutrientsCard {
    data class State(
        val kCalories: StringSource = StringSource.Empty,
        val proteins: StringSource = StringSource.Empty,
        val fats: StringSource = StringSource.Empty,
        val carbohydrates: StringSource = StringSource.Empty,
    )

    sealed interface Event {
        data class KCaloriesChanged(
            val value: String,
        ) : Event

        data class ProteinsChanged(
            val value: String,
        ) : Event

        data class FatsChanged(
            val value: String,
        ) : Event

        data class CarbohydratesChanged(
            val value: String,
        ) : Event
    }
}

@Composable
fun NutrientsCard(
    state: NutrientsCard.State,
    onEvent: (NutrientsCard.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                NutrientField(
                    value = state.kCalories,
                    onValueChange = { onEvent(NutrientsCard.Event.KCaloriesChanged(it)) },
                    label = StringSource.Resource(R.string.nutrients_card_calories),
                    modifier = Modifier.weight(1f),
                )
                NutrientField(
                    value = state.proteins,
                    onValueChange = { onEvent(NutrientsCard.Event.ProteinsChanged(it)) },
                    label = StringSource.Resource(R.string.nutrients_card_proteins),
                    modifier = Modifier.weight(1f),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                NutrientField(
                    value = state.fats,
                    onValueChange = { onEvent(NutrientsCard.Event.FatsChanged(it)) },
                    label = StringSource.Resource(R.string.nutrients_card_fats),
                    modifier = Modifier.weight(1f),
                )
                NutrientField(
                    value = state.carbohydrates,
                    onValueChange = { onEvent(NutrientsCard.Event.CarbohydratesChanged(it)) },
                    label = StringSource.Resource(R.string.nutrients_card_carbohydrates),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun NutrientField(
    value: StringSource,
    onValueChange: (String) -> Unit,
    label: StringSource,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value.get(),
        onValueChange = onValueChange,
        label = {
            Text(
                text = label.get(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        singleLine = true,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
        modifier = modifier,
    )
}

@Composable
@PreviewLightDark
private fun NutrientsCardPreview(
    @PreviewParameter(NutrientsCardPreviewParameterProvider::class)
    state: NutrientsCard.State,
) {
    NyamTrackerTheme {
        NutrientsCard(
            state = state,
            onEvent = {},
        )
    }
}

private class NutrientsCardPreviewParameterProvider : PreviewParameterProvider<NutrientsCard.State> {
    override val values: Sequence<NutrientsCard.State> =
        sequenceOf(
            NutrientsCard.State(
                kCalories = "379".stringSource,
                proteins = "8.5".stringSource,
                fats = "1.8".stringSource,
                carbohydrates = "81.0".stringSource,
            ),
            NutrientsCard.State(
                kCalories = StringSource.Empty,
                proteins = StringSource.Empty,
                fats = StringSource.Empty,
                carbohydrates = StringSource.Empty,
            ),
        )
}
