package com.bavian.nyam.tracker.ui.components.picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

object NumberPickerRoulette {
    data class State(
        val list: ImmutableList<Int>,
        val value: Int,
    )

    sealed interface Event {
        data class ValueChanged(
            val value: Int,
        ) : Event
    }
}

@Composable
fun NumberPickerRoulette(
    state: NumberPickerRoulette.State,
    onEvent: (NumberPickerRoulette.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.list.isEmpty()) return

    val coroutineScope = rememberCoroutineScope()
    val infinitePageCount = Int.MAX_VALUE
    val initialIndex = state.list.indexOf(state.value).coerceAtLeast(0)
    val initialPage =
        remember(state.list) {
            (infinitePageCount / 2) - ((infinitePageCount / 2) % state.list.size) + initialIndex
        }

    val pagerState =
        rememberPagerState(
            initialPage = initialPage,
        ) {
            infinitePageCount
        }

    LaunchedEffect(state.value) {
        val currentIndex = pagerState.currentPage % state.list.size
        val targetIndex = state.list.indexOf(state.value).coerceAtLeast(0)
        if (currentIndex != targetIndex) {
            val diff = targetIndex - currentIndex
            val halfSize = state.list.size / 2
            val optimizedDiff =
                when {
                    diff > halfSize -> diff - state.list.size
                    diff < -halfSize -> diff + state.list.size
                    else -> diff
                }
            pagerState.animateScrollToPage(pagerState.currentPage + optimizedDiff)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val newValue = state.list[page % state.list.size]
            if (newValue != state.value) {
                onEvent(NumberPickerRoulette.Event.ValueChanged(newValue))
            }
        }
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier,
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.height(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            pageSize = PageSize.Fixed(50.dp),
            contentPadding = PaddingValues(vertical = 50.dp),
        ) { page ->
            val index = page % state.list.size
            Box(
                modifier =
                    Modifier
                        .graphicsLayer {
                            val pageOffset =
                                pagerState.currentPage
                                    .minus(page)
                                    .plus(pagerState.currentPageOffsetFraction)
                                    .absoluteValue

                            alpha =
                                lerp(
                                    start = 0.3f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f),
                                )
                            scaleX =
                                lerp(
                                    start = 0.7f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f),
                                )
                            scaleY =
                                lerp(
                                    start = 0.7f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f),
                                )
                        }.height(50.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page)
                            }
                        },
                contentAlignment = Alignment.Center,
            ) {
                val selected = pagerState.currentPage == page

                if (selected) {
                    NumberDivider(Alignment.TopCenter)
                }

                Text(
                    text = state.list[index].toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                )

                if (selected) {
                    NumberDivider(Alignment.BottomCenter)
                }
            }
        }
    }
}

@Composable
private fun BoxScope.NumberDivider(alignment: Alignment) {
    HorizontalDivider(
        modifier =
            Modifier
                .align(alignment)
                .padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
    )
}

@Preview(showBackground = true)
@Composable
private fun NumberPickerRoulettePreview(
    @PreviewParameter(NumberPickerRoulettePreviewParameterProvider::class)
    state: NumberPickerRoulette.State,
) {
    NumberPickerRoulette(
        state = state,
        onEvent = {},
        modifier = Modifier.padding(16.dp).width(100.dp),
    )
}

private class NumberPickerRoulettePreviewParameterProvider : PreviewParameterProvider<NumberPickerRoulette.State> {
    override val values: Sequence<NumberPickerRoulette.State> =
        sequenceOf(
            NumberPickerRoulette.State(
                list = 0.rangeTo(10).toImmutableList(),
                value = 5,
            ),
            NumberPickerRoulette.State(
                list = 10.rangeTo(100).step(10).toImmutableList(),
                value = 50,
            ),
            NumberPickerRoulette.State(
                list = 1.rangeTo(5).toImmutableList(),
                value = 1,
            ),
        )
}
