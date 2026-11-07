package com.bavian.nyam.tracker.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.Instant

object WeightChart {
    data class WeightPoint(
        val timestamp: Long,
        val weight: Float,
    )

    data class State(
        val weightPoints: ImmutableList<WeightPoint> = persistentListOf(),
    )
}

@Composable
fun WeightChart(
    state: WeightChart.State,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        val points = state.weightPoints
        val textMeasurer = rememberTextMeasurer()
        val textStyle = TextStyle(color = textColor, fontSize = 10.sp)
        val valueTextStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 10.sp)

        Canvas(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(16.dp),
        ) {
            if (points.isEmpty()) return@Canvas

            val leftPadding = 48.dp.toPx()
            val bottomPadding = 32.dp.toPx()
            val topPadding = 24.dp.toPx()
            val rightPadding = 16.dp.toPx()

            val chartWidth = size.width - leftPadding - rightPadding
            val chartHeight = size.height - topPadding - bottomPadding

            if (chartWidth <= 0f || chartHeight <= 0f) return@Canvas

            val minX = points.minOf { it.timestamp }.toFloat()
            val maxX = points.maxOf { it.timestamp }.toFloat()
            val rangeX = if (maxX == minX) 1f else maxX - minX

            val minY = points.minOf { it.weight }
            val maxY = points.maxOf { it.weight }
            val yPadding = ((maxY - minY) * 0.1f).coerceAtLeast(0.5f)
            val adjustedMinY = minY - yPadding
            val adjustedMaxY = maxY + yPadding
            val rangeY = if (adjustedMaxY == adjustedMinY) 1f else adjustedMaxY - adjustedMinY

            // Draw Y-axis grid lines and labels
            val steps = 4
            for (i in 0..steps) {
                val fraction = i / steps.toFloat()
                val weightVal = adjustedMaxY - fraction * rangeY
                val y = topPadding + fraction * chartHeight

                // Grid line
                drawLine(
                    color = textColor.copy(alpha = 0.2f),
                    start = Offset(leftPadding, y),
                    end = Offset(leftPadding + chartWidth, y),
                    strokeWidth = 1.dp.toPx(),
                )

                // Y-axis label
                val labelText = String.format(Locale.getDefault(), "%.1f", weightVal)
                val textLayoutResult = textMeasurer.measure(labelText, style = textStyle)
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(leftPadding - textLayoutResult.size.width - 8.dp.toPx(), y - textLayoutResult.size.height / 2),
                )
            }

            // Draw X-axis line
            drawLine(
                color = textColor.copy(alpha = 0.5f),
                start = Offset(leftPadding, topPadding + chartHeight),
                end = Offset(leftPadding + chartWidth, topPadding + chartHeight),
                strokeWidth = 1.5.dp.toPx(),
            )

            val path = Path()
            val coordinates =
                points.map { point ->
                    val x = leftPadding + if (rangeX == 0f) chartWidth / 2f else (point.timestamp - minX) / rangeX * chartWidth
                    val y = topPadding + chartHeight - (point.weight - adjustedMinY) / rangeY * chartHeight
                    Offset(x, y)
                }

            coordinates.forEachIndexed { index, offset ->
                if (index == 0) {
                    path.moveTo(offset.x, offset.y)
                } else {
                    path.lineTo(offset.x, offset.y)
                }
            }

            // Draw line graph
            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 3.dp.toPx()),
            )

            // Draw points, values, and X-axis time labels
            coordinates.forEachIndexed { index, offset ->
                val point = points[index]
                // Draw point circle
                drawCircle(
                    color = lineColor,
                    radius = 4.dp.toPx(),
                    center = offset,
                )

                // Draw weight value above point
                val weightText = String.format(Locale.getDefault(), "%.1f", point.weight)
                val weightLayout = textMeasurer.measure(weightText, style = valueTextStyle)
                drawText(
                    textLayoutResult = weightLayout,
                    topLeft = Offset(offset.x - weightLayout.size.width / 2, offset.y - weightLayout.size.height - 8.dp.toPx()),
                )

                // Draw X-axis time label (date)
                val localDate =
                    Instant
                        .fromEpochMilliseconds(point.timestamp)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                val dateText = String.format(Locale.getDefault(), "%02d.%02d", localDate.day, localDate.month.ordinal + 1)
                val dateLayout = textMeasurer.measure(dateText, style = textStyle)
                drawText(
                    textLayoutResult = dateLayout,
                    topLeft = Offset(offset.x - dateLayout.size.width / 2, topPadding + chartHeight + 6.dp.toPx()),
                )
            }
        }
    }
}

private class WeightChartPreviewParameterProvider : PreviewParameterProvider<WeightChart.State> {
    override val values =
        sequenceOf(
            WeightChart.State(
                weightPoints =
                    persistentListOf(
                        WeightChart.WeightPoint(1710000000000L, 70.5f),
                        WeightChart.WeightPoint(1710086400000L, 70.2f),
                        WeightChart.WeightPoint(1710172800000L, 70.0f),
                        WeightChart.WeightPoint(1710259200000L, 69.8f),
                        WeightChart.WeightPoint(1710345600000L, 69.5f),
                    ),
            ),
        )
}

@PreviewLightDark
@Composable
private fun WeightChartPreview(
    @PreviewParameter(WeightChartPreviewParameterProvider::class) state: WeightChart.State,
) {
    NyamTrackerTheme {
        WeightChart(state = state)
    }
}
