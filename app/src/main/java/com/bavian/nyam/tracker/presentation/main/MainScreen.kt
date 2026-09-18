package com.bavian.nyam.tracker.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    var showMenu by remember { mutableStateOf(false) }

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
                    Text(text = stringResource(R.string.main_scan_barcode))
                }

                Button(
                    onClick = { onEvent(MainScreenEvent.AddProductTap) },
                ) {
                    Text(text = stringResource(R.string.main_add_product))
                }

                IconButton(
                    onClick = { showMenu = true },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.main_menu_description),
                    )

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.main_products_list)) },
                            onClick = {
                                showMenu = false
                                onEvent(MainScreenEvent.ProductsListTap)
                            },
                        )
                    }
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
