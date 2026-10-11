package com.bavian.nyam.tracker.ui.components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.model.StringSource
import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

object FoodListCard {
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

    data class State(
        val title: StringSource = StringSource.Empty,
        val items: ImmutableList<Item> = persistentListOf(),
    )

    sealed interface Event {
        data class ItemClicked(
            val id: String,
        ) : Event
    }
}

@Composable
fun FoodListCard(
    state: FoodListCard.State,
    onEvent: (FoodListCard.Event) -> Unit,
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
                text = state.title.get(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
            )

            state.items.forEachIndexed { index, item ->
                if (index > 0) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                    )
                }
                FoodListItemView(
                    item = item,
                    onClick = { onEvent(FoodListCard.Event.ItemClicked(item.id)) },
                )
            }
        }
    }
}

@Composable
private fun FoodListItemView(
    item: FoodListCard.Item,
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
            FoodNutrientInfo(
                label = stringResource(R.string.eaten_food_weight_prefix),
                value = item.weight,
            )
            FoodNutrientInfo(
                label = stringResource(R.string.products_list_calories_prefix),
                value = item.calories,
            )
            FoodNutrientInfo(
                label = stringResource(R.string.products_list_proteins_prefix),
                value = item.proteins,
            )
            FoodNutrientInfo(
                label = stringResource(R.string.products_list_fat_prefix),
                value = item.fat,
            )
            FoodNutrientInfo(
                label = stringResource(R.string.products_list_carbohydrates_prefix),
                value = item.carbohydrates,
            )
        }
    }
}

@Composable
private fun FoodNutrientInfo(
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
@PreviewLightDark
private fun FoodListCardPreview(
    @PreviewParameter(FoodListCardPreviewParameterProvider::class)
    state: FoodListCard.State,
) {
    NyamTrackerTheme {
        FoodListCard(
            state = state,
            onEvent = {},
        )
    }
}

private class FoodListCardPreviewParameterProvider : PreviewParameterProvider<FoodListCard.State> {
    override val values: Sequence<FoodListCard.State> =
        sequenceOf(
            FoodListCard.State(
                title = "08:00 - 09:00".stringSource,
                items =
                    persistentListOf(
                        FoodListCard.Item(
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
            FoodListCard.State(
                title = "18:00 - 19:00".stringSource,
                items =
                    persistentListOf(
                        FoodListCard.Item(
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
                        FoodListCard.Item(
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
        )
}
