package com.bavian.nyam.tracker.ui.components.calories

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme

object CaloriesSummary {
    const val MIN_CALORIES = 0f
    const val MID_CALORIES = 2000f
    const val HIGH_CALORIES = 2500f
    const val MAX_CALORIES = 3000f

    const val MIN_SCALE = 1.0f
    const val MAX_SCALE = 1.4f

    data class State(
        val calories: Float,
    )
}

@Composable
fun CaloriesSummary(
    state: CaloriesSummary.State,
    modifier: Modifier = Modifier,
) {
    CaloriesSummary(
        calories = state.calories,
        modifier = modifier,
    )
}

@Composable
fun CaloriesSummary(
    calories: Float,
    modifier: Modifier = Modifier,
) {
    val clampedCalories = calories.coerceIn(CaloriesSummary.MIN_CALORIES, CaloriesSummary.MAX_CALORIES)
    val progress = clampedCalories / CaloriesSummary.MAX_CALORIES

    val targetScale = CaloriesSummary.MIN_SCALE + ((CaloriesSummary.MAX_SCALE - CaloriesSummary.MIN_SCALE) * progress)
    val scale by animateFloatAsState(targetValue = targetScale, label = "CaloriesScale")

    val blue = Color(0xFF2196F3)
    val green = Color(0xFF4CAF50)
    val yellow = Color(0xFFFFC107)
    val red = Color(0xFFF44336)

    val color =
        when {
            calories <= CaloriesSummary.MIN_CALORIES -> blue
            calories <= CaloriesSummary.MID_CALORIES -> lerp(blue, green, calories / CaloriesSummary.MID_CALORIES)
            calories <= CaloriesSummary.HIGH_CALORIES ->
                lerp(
                    green,
                    yellow,
                    (calories - CaloriesSummary.MID_CALORIES) / (CaloriesSummary.HIGH_CALORIES - CaloriesSummary.MID_CALORIES),
                )
            calories <= CaloriesSummary.MAX_CALORIES ->
                lerp(
                    yellow,
                    red,
                    (calories - CaloriesSummary.HIGH_CALORIES) / (CaloriesSummary.MAX_CALORIES - CaloriesSummary.HIGH_CALORIES),
                )
            else -> red
        }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.calories_summary_format, calories.toInt()),
            color = color,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.scale(scale),
        )
    }
}

@Composable
@PreviewLightDark
private fun CaloriesSummaryPreview(
    @PreviewParameter(CaloriesSummaryPreviewParameterProvider::class)
    state: CaloriesSummary.State,
) {
    NyamTrackerTheme {
        CaloriesSummary(
            state = state,
        )
    }
}

private class CaloriesSummaryPreviewParameterProvider : PreviewParameterProvider<CaloriesSummary.State> {
    override val values: Sequence<CaloriesSummary.State> =
        sequenceOf(0f, 1000f, 2000f, 2250f, 2500f, 2750f, 3000f, 3500f).map(CaloriesSummary::State)
}
