package com.bavian.nyam.tracker.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.presentation.components.DateCarousel

@Composable
fun MainScreen(
    state: MainUiState,
    onEvent: (MainScreenEvent) -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = { onEvent(MainScreenEvent.StartScanTap) },
                ) {
                    Text(text = "Scan Barcode")
                }

                Button(
                    onClick = { onEvent(MainScreenEvent.AddProductTap) },
                ) {
                    Text(text = "Add Product")
                }
            }

            DateCarousel(
                state = DateCarousel.State(state.selectedDate),
                onEvent = { event ->
                    when (event) {
                        is DateCarousel.Event.DatePicked -> {
                            onEvent(MainScreenEvent.CalendarDatePicked(event.date))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun MainScreenPreview(
    @PreviewParameter(MainScreenPreviewParameterProvider::class)
    state: MainUiState,
) {
    MainScreen(
        state = state,
        onEvent = {},
    )
}

private class MainScreenPreviewParameterProvider : PreviewParameterProvider<MainUiState> {
    override val values: Sequence<MainUiState> =
        sequenceOf(
            MainUiState(),
        )
}
