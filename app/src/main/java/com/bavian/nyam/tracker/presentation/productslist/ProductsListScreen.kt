package com.bavian.nyam.tracker.presentation.productslist

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.presentation.components.ProductCard
import com.bavian.nyam.tracker.presentation.productslist.model.ProductsListProduct

@Composable
fun ProductsListScreen(
    state: ProductsListUiState,
    onEvent: (ProductsListEvent) -> Unit,
) {
    val context = LocalContext.current
    val toastMessage = stringResource(R.string.products_list_not_implemented)

    Scaffold(
        topBar = {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onEvent(ProductsListEvent.SearchQueryChanged(it)) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                placeholder = { Text(stringResource(R.string.products_list_search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.products, key = { it.id }) { product ->
                ProductCard(
                    state =
                        ProductCard.State(
                            id = product.id,
                            manufacturer = product.manufacturer,
                            name = product.name,
                            calories = product.calories,
                            proteins = product.proteins,
                            fat = product.fat,
                            carbohydrates = product.carbohydrates,
                            menuExpanded = state.expandedProductId == product.id,
                        ),
                    onEvent = { event ->
                        when (event) {
                            ProductCard.Event.Clicked -> {
                                onEvent(ProductsListEvent.ProductClicked(product))
                                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                            }
                            ProductCard.Event.EditClicked -> onEvent(ProductsListEvent.EditProductClicked(product))
                            ProductCard.Event.DeleteClicked -> onEvent(ProductsListEvent.DeleteProductClicked(product))
                            ProductCard.Event.MenuClicked -> onEvent(ProductsListEvent.ContextMenuClicked(product))
                            ProductCard.Event.DismissMenu -> onEvent(ProductsListEvent.DismissContextMenu)
                        }
                    },
                )
            }
        }

        state.deleteConfirmationProduct?.let { product ->
            AlertDialog(
                onDismissRequest = { onEvent(ProductsListEvent.DeleteProductCancelled) },
                title = { Text(text = stringResource(R.string.products_list_delete_dialog_title)) },
                text = {
                    Text(
                        text =
                            stringResource(
                                R.string.products_list_delete_dialog_message,
                                product.name,
                                product.manufacturer,
                            ),
                    )
                },
                confirmButton = {
                    TextButton(onClick = { onEvent(ProductsListEvent.DeleteProductConfirmed) }) {
                        Text(text = stringResource(R.string.products_list_delete_dialog_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(ProductsListEvent.DeleteProductCancelled) }) {
                        Text(text = stringResource(R.string.products_list_delete_dialog_cancel))
                    }
                },
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun ProductsListScreenPreview(
    @PreviewParameter(ProductsListScreenPreviewParameterProvider::class)
    state: ProductsListUiState,
) {
    ProductsListScreen(
        state = state,
    ) {}
}

private class ProductsListScreenPreviewParameterProvider : PreviewParameterProvider<ProductsListUiState> {
    override val values: Sequence<ProductsListUiState> =
        sequenceOf(
            ProductsListUiState(
                products =
                    listOf(
                        ProductsListProduct("1", "Nestle", "Nesquik", "379.0", "8.5", "1.8", "81.0"),
                        ProductsListProduct("2", "Coca-Cola", "Cola", "42.0", "0.0", "0.0", "10.6"),
                    ),
            ),
            ProductsListUiState(
                searchQuery = "Nes",
                products =
                    listOf(
                        ProductsListProduct("1", "Nestle", "Nesquik", "379.0", "8.5", "1.8", "81.0"),
                    ),
                expandedProductId = "1",
            ),
        )
}
