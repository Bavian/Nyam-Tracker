package com.bavian.nyam.tracker.ui.components.loader

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.ui.preview.PreviewScreen
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme

@Composable
fun SnakeLoaderWrapper(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    loaderSize: Dp = 80.dp,
    content: (@Composable () -> Unit)? = null,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (content != null) {
            val contentModifier =
                if (isLoading && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)) {
                    Modifier.blur(12.dp)
                } else {
                    Modifier
                }
            Box(modifier = contentModifier.fillMaxSize()) {
                content()
            }
        }

        if (isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {},
                contentAlignment = Alignment.Center,
            ) {
                SnakeLoader(
                    size = loaderSize,
                )
            }
        }
    }
}

@PreviewScreen
@Composable
private fun SnakeLoaderWrapperPreview() {
    NyamTrackerTheme {
        SnakeLoaderWrapper(
            isLoading = true,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Main Screen Content",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}
