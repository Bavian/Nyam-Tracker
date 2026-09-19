package com.bavian.nyam.tracker.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.presentation.components.DateCarousel

@Composable
fun MainScreen(
    state: MainUiState,
    onEvent: (MainScreenEvent) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                FloatingActionButton(
                    onClick = { onEvent(MainScreenEvent.ProductsListTap) },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_add_2),
                        contentDescription = stringResource(R.string.main_products_list),
                    )
                }

                FloatingActionButton(
                    onClick = { onEvent(MainScreenEvent.StartScanTap) },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_barcode_scanner),
                        contentDescription = stringResource(R.string.main_scan_barcode),
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        ) {
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
