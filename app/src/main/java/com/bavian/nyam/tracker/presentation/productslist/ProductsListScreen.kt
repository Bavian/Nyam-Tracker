package com.bavian.nyam.tracker.presentation.productslist

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.presentation.components.ProductCard
import com.bavian.nyam.tracker.presentation.productslist.model.ProductsListProduct

@Composable
fun ProductsListScreen(
    state: ProductsListUiState,
    onEvent: (ProductsListEvent) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    onEvent(ProductsListEvent.ScreenStarted)
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val context = LocalContext.current
    val toastMessage = stringResource(R.string.products_list_not_implemented)

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            Row(
                modifier =
                    Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { onEvent(ProductsListEvent.BackClicked) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_arrow_back),
                        contentDescription = stringResource(R.string.set_product_back_description),
                    )
                }

                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { onEvent(ProductsListEvent.SearchQueryChanged(it)) },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    placeholder = { Text(stringResource(R.string.products_list_search_hint)) },
                    leadingIcon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_24dp_search), contentDescription = null) },
                    singleLine = true,
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(ProductsListEvent.AddProductClicked) },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_add_2),
                    contentDescription = stringResource(R.string.products_list_add_product),
                )
            }
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
