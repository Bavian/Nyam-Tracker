package com.bavian.nyam.tracker.ui.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.theme.NutrientCarbohydrateColor
import com.bavian.nyam.tracker.ui.theme.NutrientFatColor
import com.bavian.nyam.tracker.ui.theme.NutrientProteinColor
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.pie.PieChart
import com.patrykandpatrick.vico.compose.pie.PieChartHost
import com.patrykandpatrick.vico.compose.pie.PieSize
import com.patrykandpatrick.vico.compose.pie.data.PieChartModel
import com.patrykandpatrick.vico.compose.pie.rememberPieChart
import java.util.Locale

object ProductCard {
    data class State(
        val id: String,
        val manufacturer: String,
        val name: String,
        val calories: String,
        val proteins: String,
        val fat: String,
        val carbohydrates: String,
        val weight: String = "150 г",
        val time: String = "11:15",
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

private val ProteinsColor = NutrientProteinColor
private val FatsColor = NutrientFatColor
private val CarbohydratesColor = NutrientCarbohydrateColor

@Composable
fun ProductCard(
    state: ProductCard.State,
    onEvent: (ProductCard.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Card(
        onClick = { onEvent(ProductCard.Event.Clicked) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = colors.surfaceContainerLow,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            // Product title, manufacturer, time and menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = state.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = state.manufacturer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    text = state.time,
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.onSurfaceVariant,
                )

                Box {
                    IconButton(
                        onClick = {
                            onEvent(ProductCard.Event.MenuClicked)
                        },
                    ) {
                        Icon(
                            imageVector =
                                ImageVector.vectorResource(
                                    id = R.drawable.ic_24dp_more_vert,
                                ),
                            contentDescription =
                                stringResource(
                                    R.string.products_list_context_menu_description,
                                ),
                        )
                    }

                    DropdownMenu(
                        expanded = state.menuExpanded,
                        onDismissRequest = {
                            onEvent(ProductCard.Event.DismissMenu)
                        },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text =
                                        stringResource(
                                            R.string.products_list_edit,
                                        ),
                                )
                            },
                            onClick = {
                                onEvent(ProductCard.Event.EditClicked)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector =
                                        ImageVector.vectorResource(
                                            id = R.drawable.ic_24dp_edit,
                                        ),
                                    contentDescription = null,
                                )
                            },
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text =
                                        stringResource(
                                            R.string.products_list_delete,
                                        ),
                                    color = colors.error,
                                )
                            },
                            onClick = {
                                onEvent(ProductCard.Event.DeleteClicked)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector =
                                        ImageVector.vectorResource(
                                            id = R.drawable.ic_24dp_delete_forever,
                                        ),
                                    contentDescription = null,
                                    tint = colors.error,
                                )
                            },
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            val proteinsVal = state.proteins.toNutritionValue()
            val fatVal = state.fat.toNutritionValue()
            val carbohydratesVal = state.carbohydrates.toNutritionValue()

            val proteinCalories = proteinsVal * 4.0
            val fatCalories = fatVal * 9.0
            val carbohydrateCalories = carbohydratesVal * 4.0

            val totalMacroCalories = proteinCalories + fatCalories + carbohydrateCalories
            val proteinPct = if (totalMacroCalories > 0.0) (proteinCalories / totalMacroCalories) * 100.0 else 0.0
            val fatPct = if (totalMacroCalories > 0.0) (fatCalories / totalMacroCalories) * 100.0 else 0.0
            val carbohydratePct = if (totalMacroCalories > 0.0) (carbohydrateCalories / totalMacroCalories) * 100.0 else 0.0

            // Macro chart, nutrients and consumed weight
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MacroDonutChart(
                    calories = state.calories.toNutritionValue(),
                    proteinPct = proteinPct,
                    fatPct = fatPct,
                    carbohydratePct = carbohydratePct,
                    modifier = Modifier.size(90.dp),
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = state.weight,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface,
                        maxLines = 1,
                    )

                    Spacer(Modifier.height(2.dp))

                    NutrientRow(
                        name = stringResource(R.string.nutrients_diagram_proteins),
                        value = "${state.proteins} г",
                        percentage = proteinPct.toFloat(),
                        color = ProteinsColor,
                    )
                    NutrientRow(
                        name = stringResource(R.string.nutrients_diagram_fats),
                        value = "${state.fat} г",
                        percentage = fatPct.toFloat(),
                        color = FatsColor,
                    )
                    NutrientRow(
                        name = stringResource(R.string.nutrients_diagram_carbohydrates),
                        value = "${state.carbohydrates} г",
                        percentage = carbohydratePct.toFloat(),
                        color = CarbohydratesColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun NutrientRow(
    name: String,
    value: String,
    percentage: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color),
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            text = "$value (${formatPercentage(percentage)})",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
    }
}

@Composable
private fun MacroDonutChart(
    calories: Double,
    proteinPct: Double,
    fatPct: Double,
    carbohydratePct: Double,
    modifier: Modifier = Modifier,
) {
    val totalPct = proteinPct + fatPct + carbohydratePct

    val pieChart =
        rememberPieChart(
            sliceProvider =
                PieChart.SliceProvider.series(
                    listOf(
                        PieChart.Slice(fill = Fill(ProteinsColor)),
                        PieChart.Slice(fill = Fill(FatsColor)),
                        PieChart.Slice(fill = Fill(CarbohydratesColor)),
                    ),
                ),
            spacing = 2.dp,
            outerSize = PieSize.Outer.Fill,
            innerSize = PieSize.Inner.fixed(64.dp),
            startAngle = -90f,
        )

    val model =
        remember(proteinPct, fatPct, carbohydratePct) {
            if (totalPct > 0.0) {
                PieChartModel.build(proteinPct, fatPct, carbohydratePct)
            } else {
                PieChartModel.build(0f, 0f, 0f)
            }
        }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        if (totalPct > 0.0) {
            PieChartHost(
                chart = pieChart,
                model = model,
                modifier = Modifier.matchParentSize(),
            )
        } else {
            Box(
                modifier =
                    Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = calories.toDisplayString(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "ккал",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun formatPercentage(percentage: Float): String =
    if ((percentage % 1f) == 0f) {
        "${percentage.toInt()}%"
    } else {
        "${String.format(Locale.getDefault(), "%.1f", percentage)}%"
    }

private fun String.toNutritionValue(): Double =
    replace(',', '.')
        .trim()
        .toDoubleOrNull()
        ?.coerceAtLeast(0.0)
        ?: 0.0

private fun Double.toDisplayString(): String =
    if (isFinite()) {
        if (this % 1.0 == 0.0) {
            toLong().toString()
        } else {
            "%.1f".format(Locale.getDefault(), this)
        }
    } else {
        "0"
    }

@PreviewLightDark
@Composable
private fun ProductCardPreview(
    @PreviewParameter(ProductCardPreviewParameterProvider::class)
    state: ProductCard.State,
) {
    NyamTrackerTheme {
        ProductCard(
            state = state,
            onEvent = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

private class ProductCardPreviewParameterProvider : PreviewParameterProvider<ProductCard.State> {
    override val values: Sequence<ProductCard.State> =
        sequenceOf(
            ProductCard.State(
                id = "1",
                manufacturer = "Danone",
                name = "Греческий йогурт",
                calories = "234",
                proteins = "18.8",
                fat = "12.0",
                carbohydrates = "9.6",
                weight = "150 г",
                time = "11:15",
            ),
            ProductCard.State(
                id = "2",
                manufacturer = "Coca-Cola",
                name = "Coca-Cola без сахара",
                calories = "1",
                proteins = "0.0",
                fat = "0.0",
                carbohydrates = "0.0",
                weight = "330 г",
                time = "14:30",
                menuExpanded = true,
            ),
        )
}
