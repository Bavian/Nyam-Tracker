package com.bavian.nyam.tracker.ui.components.diagram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
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

object NutrientsDiagram {
    data class State(
        val proteins: Float = 0f,
        val fats: Float = 0f,
        val carbohydrates: Float = 0f,
    )

    data class Colors(
        val proteins: Color = NutrientProteinColor,
        val fats: Color = NutrientFatColor,
        val carbohydrates: Color = NutrientCarbohydrateColor,
    )
}

@Composable
fun NutrientsDiagram(
    state: NutrientsDiagram.State,
    modifier: Modifier = Modifier,
    colors: NutrientsDiagram.Colors = NutrientsDiagram.Colors(),
    diagramSize: Dp = 80.dp,
) {
    val totalWeight = state.proteins + state.fats + state.carbohydrates
    val proteinsPercentage = if (totalWeight > 0f) (state.proteins / totalWeight) * 100f else 0f
    val fatsPercentage = if (totalWeight > 0f) (state.fats / totalWeight) * 100f else 0f
    val carbohydratesPercentage =
        if (totalWeight > 0f) (state.carbohydrates / totalWeight) * 100f else 0f

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier =
                Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            NutrientsPieChart(
                state = state,
                totalWeight = totalWeight,
                colors = colors,
                diagramSize = diagramSize,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                NutrientSectorLabel(
                    name = stringResource(R.string.nutrients_diagram_proteins),
                    weight = state.proteins,
                    percentage = proteinsPercentage,
                    color = colors.proteins,
                )
                NutrientSectorLabel(
                    name = stringResource(R.string.nutrients_diagram_fats),
                    weight = state.fats,
                    percentage = fatsPercentage,
                    color = colors.fats,
                )
                NutrientSectorLabel(
                    name = stringResource(R.string.nutrients_diagram_carbohydrates),
                    weight = state.carbohydrates,
                    percentage = carbohydratesPercentage,
                    color = colors.carbohydrates,
                )
            }
        }
    }
}

@Composable
private fun NutrientsPieChart(
    state: NutrientsDiagram.State,
    totalWeight: Float,
    colors: NutrientsDiagram.Colors,
    diagramSize: Dp,
    modifier: Modifier = Modifier,
) {
    val emptyTrackColor = MaterialTheme.colorScheme.surfaceVariant

    if (totalWeight <= 0f) {
        Box(
            modifier =
                modifier
                    .size(diagramSize)
                    .clip(CircleShape)
                    .background(emptyTrackColor),
        )
        return
    }

    val pieChart =
        rememberPieChart(
            sliceProvider =
                PieChart.SliceProvider.series(
                    listOf(
                        PieChart.Slice(fill = Fill(colors.proteins)),
                        PieChart.Slice(fill = Fill(colors.fats)),
                        PieChart.Slice(fill = Fill(colors.carbohydrates)),
                    ),
                ),
            spacing = 2.dp,
            outerSize = PieSize.Outer.Fill,
            innerSize = PieSize.Inner.Zero,
            startAngle = -90f,
        )

    val model =
        remember(state.proteins, state.fats, state.carbohydrates) {
            PieChartModel.build(state.proteins, state.fats, state.carbohydrates)
        }

    PieChartHost(
        chart = pieChart,
        model = model,
        modifier = modifier.size(diagramSize),
        chartAreaHeight = diagramSize,
    )
}

@Composable
private fun NutrientSectorLabel(
    name: String,
    weight: Float,
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
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color),
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
        }
        Text(
            text = "${formatWeight(weight)} (${formatPercentage(percentage)})",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun formatWeight(value: Float): String =
    if ((value % 1f) == 0f) {
        "${value.toInt()} g"
    } else {
        "${String.format(Locale.getDefault(), "%.1f", value)} g"
    }

private fun formatPercentage(percentage: Float): String =
    if ((percentage % 1f) == 0f) {
        "${percentage.toInt()}%"
    } else {
        "${String.format(Locale.getDefault(), "%.1f", percentage)}%"
    }

@Composable
@PreviewLightDark
private fun NutrientsDiagramPreview(
    @PreviewParameter(NutrientsDiagramPreviewParameterProvider::class)
    state: NutrientsDiagram.State,
) {
    NyamTrackerTheme {
        NutrientsDiagram(
            state = state,
            modifier = Modifier.padding(16.dp),
        )
    }
}

private class NutrientsDiagramPreviewParameterProvider : PreviewParameterProvider<NutrientsDiagram.State> {
    override val values: Sequence<NutrientsDiagram.State> =
        sequenceOf(
            NutrientsDiagram.State(
                proteins = 25f,
                fats = 15f,
                carbohydrates = 60f,
            ),
            NutrientsDiagram.State(
                proteins = 30.5f,
                fats = 0f,
                carbohydrates = 45f,
            ),
            NutrientsDiagram.State(
                proteins = 0f,
                fats = 0f,
                carbohydrates = 0f,
            ),
        )
}
