package com.bavian.nyam.tracker.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
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

        data class EditClicked(
            val id: String,
        ) : Event

        data class DeleteClicked(
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
    var expandedItemId by remember { mutableStateOf<String?>(null) }

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

            for (item in state.items) {
                ProductCard(
                    state =
                        ProductCard.State(
                            id = item.id,
                            manufacturer = item.manufacturer,
                            name = item.name,
                            calories = item.calories,
                            proteins = item.proteins,
                            fat = item.fat,
                            carbohydrates = item.carbohydrates,
                            weight = item.weight,
                            time = item.time,
                            menuExpanded = expandedItemId == item.id,
                        ),
                    onEvent = { event ->
                        when (event) {
                            ProductCard.Event.Clicked ->
                                onEvent(FoodListCard.Event.ItemClicked(item.id))

                            ProductCard.Event.MenuClicked ->
                                expandedItemId = item.id

                            ProductCard.Event.EditClicked -> {
                                expandedItemId = null
                                onEvent(FoodListCard.Event.EditClicked(item.id))
                            }

                            ProductCard.Event.DeleteClicked -> {
                                expandedItemId = null
                                onEvent(FoodListCard.Event.DeleteClicked(item.id))
                            }

                            ProductCard.Event.DismissMenu ->
                                expandedItemId = null
                        }
                    },
                )
            }
        }
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
                            weight = "150 г",
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
                            weight = "200 г",
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
                            weight = "120 г",
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
