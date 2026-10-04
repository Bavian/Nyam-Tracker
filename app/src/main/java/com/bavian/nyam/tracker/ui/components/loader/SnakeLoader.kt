package com.bavian.nyam.tracker.ui.components.loader

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme
import kotlin.math.cos
import kotlin.math.sin

object SnakeLoader {
    const val SNAKE_COLOR = 0xFF388E3C
    const val HIGHLIGHT_COLOR = 0xFF66BB6A
    const val TONGUE_COLOR = 0xFFFF3D00

    inline val snakeColor get() = Color(SNAKE_COLOR)
    inline val highlightColor get() = Color(HIGHLIGHT_COLOR)
    inline val tongueColor get() = Color(TONGUE_COLOR)
}

@Composable
fun SnakeLoader(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SnakeLoaderTransition")

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "RotationAngle",
    )

    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 200f,
        targetValue = 270f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "SweepAngle",
    )

    val tongueFlicker by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "TongueFlicker",
    )

    Canvas(
        modifier = modifier.size(size),
    ) {
        drawSnakeLoader(
            centerX = this.size.width / 2f,
            centerY = this.size.height / 2f,
            loaderSizePx = size.toPx(),
            rotationAngle = rotationAngle,
            sweepAngle = sweepAngle,
            tongueFlicker = tongueFlicker,
            snakeColor = SnakeLoader.snakeColor,
            highlightColor = SnakeLoader.highlightColor,
            tongueColor = SnakeLoader.tongueColor,
        )
    }
}

private fun DrawScope.drawSnakeLoader(
    centerX: Float,
    centerY: Float,
    loaderSizePx: Float,
    rotationAngle: Float,
    sweepAngle: Float,
    tongueFlicker: Float,
    snakeColor: Color,
    highlightColor: Color,
    tongueColor: Color,
) {
    val strokeWidth = loaderSizePx * 0.12f
    val tongueMaxLen = loaderSizePx * 0.16f
    val padding = strokeWidth + (tongueMaxLen * 0.5f)

    val arcWidth = loaderSizePx - (padding * 2)
    val arcHeight = loaderSizePx - (padding * 2)
    val radius = arcWidth / 2f

    val arcTopLeft = Offset(centerX - radius, centerY - radius)
    val arcSize = Size(arcWidth, arcHeight)

    // 1. Shadow Arc
    drawArc(
        color = Color(0x26000000),
        startAngle = rotationAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(arcTopLeft.x + (strokeWidth * 0.15f), arcTopLeft.y + (strokeWidth * 0.2f)),
        size = arcSize,
        style = Stroke(width = strokeWidth * 1.1f, cap = StrokeCap.Round),
    )

    // 2. Main Snake Body Arc
    drawArc(
        color = snakeColor,
        startAngle = rotationAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = arcTopLeft,
        size = arcSize,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )

    // 3. Spine Highlight Arc
    drawArc(
        color = highlightColor,
        startAngle = rotationAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = arcTopLeft,
        size = arcSize,
        style = Stroke(width = strokeWidth * 0.32f, cap = StrokeCap.Round),
    )

    // 4. Snake Head at the leading edge
    val leadingAngleDeg = (rotationAngle + sweepAngle) % 360f
    val leadingAngleRad = Math.toRadians(leadingAngleDeg.toDouble())

    val headCx = centerX + (radius * cos(leadingAngleRad).toFloat())
    val headCy = centerY + (radius * sin(leadingAngleRad).toFloat())

    val forwardRotationDeg = leadingAngleDeg + 180f

    withTransform(
        transformBlock = {
            rotate(degrees = forwardRotationDeg, pivot = Offset(headCx, headCy))
        },
    ) {
        val headLen = strokeWidth * 1.5f
        val headWidth = strokeWidth * 1.3f

        val headPath =
            Path().apply {
                moveTo(headCx, headCy + (headLen * 0.5f))
                cubicTo(
                    headCx - (headWidth * 0.55f),
                    headCy + (headLen * 0.2f),
                    headCx - (headWidth * 0.5f),
                    headCy - (headLen * 0.4f),
                    headCx,
                    headCy - (headLen * 0.5f),
                )
                cubicTo(
                    headCx + (headWidth * 0.5f),
                    headCy - (headLen * 0.4f),
                    headCx + (headWidth * 0.55f),
                    headCy + (headLen * 0.2f),
                    headCx,
                    headCy + (headLen * 0.5f),
                )
                close()
            }

        drawPath(path = headPath, color = snakeColor)

        val headHighlightPath =
            Path().apply {
                moveTo(headCx, headCy + (headLen * 0.35f))
                cubicTo(
                    headCx - (headWidth * 0.35f),
                    headCy + (headLen * 0.1f),
                    headCx - (headWidth * 0.3f),
                    headCy - (headLen * 0.3f),
                    headCx,
                    headCy - (headLen * 0.4f),
                )
                cubicTo(
                    headCx + (headWidth * 0.3f),
                    headCy - (headLen * 0.3f),
                    headCx + (headWidth * 0.35f),
                    headCy + (headLen * 0.1f),
                    headCx,
                    headCy + (headLen * 0.35f),
                )
                close()
            }

        drawPath(path = headHighlightPath, color = highlightColor)

        val eyeRadius = strokeWidth * 0.18f
        val eyeXOffset = headWidth * 0.26f
        val eyeY = headCy - (headLen * 0.08f)

        // Left Eye
        drawCircle(
            color = Color.White,
            radius = eyeRadius,
            center = Offset(headCx - eyeXOffset, eyeY),
        )
        drawCircle(
            color = Color(0xFF1B1B1B),
            radius = eyeRadius * 0.55f,
            center = Offset(headCx - eyeXOffset, eyeY - (eyeRadius * 0.2f)),
        )

        // Right Eye
        drawCircle(
            color = Color.White,
            radius = eyeRadius,
            center = Offset(headCx + eyeXOffset, eyeY),
        )
        drawCircle(
            color = Color(0xFF1B1B1B),
            radius = eyeRadius * 0.55f,
            center = Offset(headCx + eyeXOffset, eyeY - (eyeRadius * 0.2f)),
        )

        val tongueLen = tongueMaxLen * tongueFlicker
        val snoutY = headCy - (headLen * 0.48f)
        val tongueTipY = snoutY - tongueLen
        val forkWidth = strokeWidth * 0.35f
        val forkHeight = strokeWidth * 0.35f

        val tonguePath =
            Path().apply {
                moveTo(headCx, snoutY)
                lineTo(headCx, tongueTipY)
                moveTo(headCx, tongueTipY)
                lineTo(headCx - forkWidth, tongueTipY - forkHeight)
                moveTo(headCx, tongueTipY)
                lineTo(headCx + forkWidth, tongueTipY - forkHeight)
            }

        drawPath(
            path = tonguePath,
            color = tongueColor,
            style =
                Stroke(
                    width = strokeWidth * 0.18f,
                    cap = StrokeCap.Round,
                ),
        )
    }
}

@PreviewLightDark
@Composable
private fun ModifierSnakeLoaderPreview() {
    NyamTrackerTheme {
        SnakeLoader()
    }
}
