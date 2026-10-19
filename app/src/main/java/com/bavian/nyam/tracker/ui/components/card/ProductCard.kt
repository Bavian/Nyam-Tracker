package com.bavian.nyam.tracker.ui.components.card

import android.graphics.Paint
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bavian.nyam.tracker.R
import kotlin.math.cos
import kotlin.math.sin

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

            Spacer(Modifier.height(20.dp))

            // Macro chart, nutrients and consumed weight
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MacroDonutChart(
                    calories = state.calories.toNutritionValue(),
                    proteins = state.proteins.toNutritionValue(),
                    fat = state.fat.toNutritionValue(),
                    carbohydrates = state.carbohydrates.toNutritionValue(),
                    modifier = Modifier.size(112.dp),
                )

                Spacer(Modifier.width(4.dp))

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NutrientColumn(
                        label =
                            stringResource(
                                R.string.products_list_proteins_prefix,
                            ),
                        value = state.proteins,
                        color = colors.primary,
                        modifier = Modifier.weight(1f),
                    )

                    NutrientColumn(
                        label =
                            stringResource(
                                R.string.products_list_fat_prefix,
                            ),
                        value = state.fat,
                        color = colors.tertiary,
                        modifier = Modifier.weight(1f),
                    )

                    NutrientColumn(
                        label =
                            stringResource(
                                R.string.products_list_carbohydrates_prefix,
                            ),
                        value = state.carbohydrates,
                        color = colors.secondary,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(Modifier.width(4.dp))

                Text(
                    text = state.weight,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun NutrientColumn(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(12.dp)
                    .padding(1.dp),
        ) {
            Canvas(Modifier.size(10.dp)) {
                drawCircle(color = color)
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = "$value г",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Clip,
        )
    }
}

@Composable
private fun MacroDonutChart(
    calories: Double,
    proteins: Double,
    fat: Double,
    carbohydrates: Double,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    val proteinCalories = proteins * 4.0
    val fatCalories = fat * 9.0
    val carbohydrateCalories = carbohydrates * 4.0

    val totalMacroCalories =
        proteinCalories + fatCalories + carbohydrateCalories

    val segments =
        listOf(
            MacroSegment(
                color = colors.primary,
                calories = proteinCalories,
            ),
            MacroSegment(
                color = colors.tertiary,
                calories = fatCalories,
            ),
            MacroSegment(
                color = colors.secondary,
                calories = carbohydrateCalories,
            ),
        )

    val trackColor = colors.surfaceVariant
    val centerTextColor = colors.onSurface

    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.18f
        val diameter = size.minDimension - strokeWidth
        val topLeft =
            Offset(
                (size.width - diameter) / 2f,
                (size.height - diameter) / 2f,
            )

        val arcSize =
            androidx.compose.ui.geometry.Size(
                diameter,
                diameter,
            )

        // Background track
        drawArc(
            color = trackColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth),
        )

        var startAngle = -90f

        segments.forEach { segment ->
            if (totalMacroCalories > 0.0) {
                val fraction = segment.calories / totalMacroCalories
                val sweepAngle = (fraction * 360.0).toFloat()

                drawArc(
                    color = segment.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style =
                        Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Butt,
                        ),
                )

                // Percentage placed inside its segment
                if (fraction >= 0.08) {
                    val middleAngle =
                        Math.toRadians(
                            (startAngle + sweepAngle / 2f).toDouble(),
                        )

                    val radius = diameter / 2f
                    val labelRadius = radius

                    val centerX = size.width / 2f
                    val centerY = size.height / 2f

                    val labelX =
                        centerX +
                            (labelRadius * cos(middleAngle)).toFloat()

                    val labelY =
                        centerY +
                            (labelRadius * sin(middleAngle)).toFloat()

                    val percentage = (fraction * 100).toInt()

                    val paint =
                        Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = android.graphics.Color.WHITE
                            textSize = 11.sp.toPx()
                            textAlign = Paint.Align.CENTER
                            typeface =
                                android.graphics.Typeface.create(
                                    android.graphics.Typeface.DEFAULT,
                                    android.graphics.Typeface.BOLD,
                                )
                        }

                    drawContext.canvas.nativeCanvas.drawText(
                        "$percentage%",
                        labelX,
                        labelY + paint.textSize * 0.35f,
                        paint,
                    )
                }

                startAngle += sweepAngle
            }
        }

        // Calories in the center
        val caloriePaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = centerTextColor.toArgb()
                textAlign = Paint.Align.CENTER
                textSize = 20.sp.toPx()
                typeface =
                    android.graphics.Typeface.create(
                        android.graphics.Typeface.DEFAULT,
                        android.graphics.Typeface.BOLD,
                    )
            }

        val unitPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = colors.onSurfaceVariant.toArgb()
                textAlign = Paint.Align.CENTER
                textSize = 12.sp.toPx()
            }

        val centerX = size.width / 2f
        val centerY = size.height / 2f

        drawContext.canvas.nativeCanvas.drawText(
            calories.toDisplayString(),
            centerX,
            centerY,
            caloriePaint,
        )

        drawContext.canvas.nativeCanvas.drawText(
            "ккал",
            centerX,
            centerY + unitPaint.textSize * 1.2f,
            unitPaint,
        )
    }
}

private data class MacroSegment(
    val color: Color,
    val calories: Double,
)

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
            "%.1f".format(java.util.Locale.US, this)
        }
    } else {
        "0"
    }

@Preview
@Composable
private fun ProductCardPreview(
    @PreviewParameter(ProductCardPreviewParameterProvider::class)
    state: ProductCard.State,
) {
    MaterialTheme {
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
