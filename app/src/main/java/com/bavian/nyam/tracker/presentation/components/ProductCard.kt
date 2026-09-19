package com.bavian.nyam.tracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R

object ProductCard {
    data class State(
        val id: String,
        val manufacturer: String,
        val name: String,
        val calories: String,
        val proteins: String,
        val fat: String,
        val carbohydrates: String,
        val menuExpanded: Boolean = false,
    )

    sealed interface Event {
        data object Clicked : Event

        data object MenuClicked : Event

        data object EditClicked : Event

        data object DeleteClicked : Event

        data object DismissMenu : Event
    }
}

@Composable
fun ProductCard(
    state: ProductCard.State,
    onEvent: (ProductCard.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onEvent(ProductCard.Event.Clicked) },
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = state.manufacturer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ProductNutrientInfo(
                        label = stringResource(R.string.products_list_calories_prefix),
                        value = state.calories,
                    )
                    ProductNutrientInfo(
                        label = stringResource(R.string.products_list_proteins_prefix),
                        value = state.proteins,
                    )
                    ProductNutrientInfo(
                        label = stringResource(R.string.products_list_fat_prefix),
                        value = state.fat,
                    )
                    ProductNutrientInfo(
                        label = stringResource(R.string.products_list_carbohydrates_prefix),
                        value = state.carbohydrates,
                    )
                }
            }
            IconButton(
                onClick = { onEvent(ProductCard.Event.MenuClicked) },
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.products_list_context_menu_description),
                )

                DropdownMenu(
                    expanded = state.menuExpanded,
                    onDismissRequest = { onEvent(ProductCard.Event.DismissMenu) },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.products_list_edit)) },
                        onClick = { onEvent(ProductCard.Event.EditClicked) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                            )
                        },
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(R.string.products_list_delete),
                                color = MaterialTheme.colorScheme.error,
                            )
                        },
                        onClick = { onEvent(ProductCard.Event.DeleteClicked) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductNutrientInfo(
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
@Preview
private fun ProductCardPreview(
    @PreviewParameter(ProductCardPreviewParameterProvider::class)
    state: ProductCard.State,
) {
    ProductCard(
        state = state,
        onEvent = {},
    )
}

private class ProductCardPreviewParameterProvider : PreviewParameterProvider<ProductCard.State> {
    override val values: Sequence<ProductCard.State> =
        sequenceOf(
            ProductCard.State(
                id = "1",
                manufacturer = "Nestle",
                name = "Nesquik",
                calories = "379.0",
                proteins = "8.5",
                fat = "1.8",
                carbohydrates = "81.0",
            ),
            ProductCard.State(
                id = "2",
                manufacturer = "Coca-Cola",
                name = "Cola",
                calories = "42.0",
                proteins = "0.0",
                fat = "0.0",
                carbohydrates = "10.6",
                menuExpanded = true,
            ),
        )
}
