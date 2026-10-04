package com.bavian.nyam.tracker.presentation.productslist

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.presentation.productslist.model.ProductsListProduct
import com.bavian.nyam.tracker.ui.components.card.ProductCard
import com.bavian.nyam.tracker.ui.components.loader.SnakeLoaderWrapper
import com.bavian.nyam.tracker.ui.preview.PreviewScreen
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

object ProductsListScreen {
    sealed interface State {
        val searchQuery: String
        val expandedProductId: String?
        val deleteConfirmationProduct: ProductsListProduct?

        data class Loading(
            override val searchQuery: String = "",
            override val expandedProductId: String? = null,
            override val deleteConfirmationProduct: ProductsListProduct? = null,
        ) : State

        data class Success(
            override val searchQuery: String = "",
            val products: ImmutableList<ProductsListProduct> = persistentListOf(),
            override val expandedProductId: String? = null,
            override val deleteConfirmationProduct: ProductsListProduct? = null,
        ) : State
    }

    sealed interface Event {
        data class SearchQueryChanged(
            val query: String,
        ) : Event

        data class ProductClicked(
            val product: ProductsListProduct,
        ) : Event

        data class EditProductClicked(
            val product: ProductsListProduct,
        ) : Event

        data class DeleteProductClicked(
            val product: ProductsListProduct,
        ) : Event

        data class ContextMenuClicked(
            val product: ProductsListProduct,
        ) : Event

        data object DismissContextMenu : Event

        data object DeleteProductConfirmed : Event

        data object DeleteProductCancelled : Event

        data object BackClicked : Event

        data object AddProductClicked : Event

        data object ScreenStarted : Event
    }
}

@Composable
fun ProductsListScreen(
    state: ProductsListScreen.State,
    onEvent: (ProductsListScreen.Event) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    onEvent(ProductsListScreen.Event.ScreenStarted)
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    ProductsListScreenContent(
        searchQuery = state.searchQuery,
        products = (state as? ProductsListScreen.State.Success)?.products ?: persistentListOf(),
        expandedProductId = state.expandedProductId,
        deleteConfirmationProduct = state.deleteConfirmationProduct,
        loading = state is ProductsListScreen.State.Loading,
        onEvent = onEvent,
    )
}

@Composable
fun ProductsListScreenContent(
    searchQuery: String,
    products: ImmutableList<ProductsListProduct>,
    expandedProductId: String?,
    deleteConfirmationProduct: ProductsListProduct?,
    loading: Boolean,
    onEvent: (ProductsListScreen.Event) -> Unit,
) {
    SnakeLoaderWrapper(
        isLoading = loading,
    ) {
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
                    IconButton(onClick = { onEvent(ProductsListScreen.Event.BackClicked) }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_arrow_back),
                            contentDescription = stringResource(R.string.set_product_back_description),
                        )
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { onEvent(ProductsListScreen.Event.SearchQueryChanged(it)) },
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        placeholder = { Text(stringResource(R.string.products_list_search_hint)) },
                        leadingIcon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_24dp_search), contentDescription = null) },
                        singleLine = true,
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onEvent(ProductsListScreen.Event.AddProductClicked) },
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
                items(products, key = { (id) -> id }) { product ->
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
                                menuExpanded = expandedProductId == product.id,
                            ),
                        onEvent = { event ->
                            when (event) {
                                ProductCard.Event.Clicked -> {
                                    onEvent(ProductsListScreen.Event.ProductClicked(product))
                                }
                                ProductCard.Event.EditClicked -> onEvent(ProductsListScreen.Event.EditProductClicked(product))
                                ProductCard.Event.DeleteClicked -> onEvent(ProductsListScreen.Event.DeleteProductClicked(product))
                                ProductCard.Event.MenuClicked -> onEvent(ProductsListScreen.Event.ContextMenuClicked(product))
                                ProductCard.Event.DismissMenu -> onEvent(ProductsListScreen.Event.DismissContextMenu)
                            }
                        },
                    )
                }
            }

            deleteConfirmationProduct?.let { product ->
                AlertDialog(
                    onDismissRequest = { onEvent(ProductsListScreen.Event.DeleteProductCancelled) },
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
                        TextButton(onClick = { onEvent(ProductsListScreen.Event.DeleteProductConfirmed) }) {
                            Text(text = stringResource(R.string.products_list_delete_dialog_confirm))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { onEvent(ProductsListScreen.Event.DeleteProductCancelled) }) {
                            Text(text = stringResource(R.string.products_list_delete_dialog_cancel))
                        }
                    },
                )
            }
        }
    }
}

@Composable
@PreviewScreen
private fun ProductsListScreenPreview(
    @PreviewParameter(ProductsListScreenPreviewParameterProvider::class)
    state: ProductsListScreen.State,
) {
    NyamTrackerTheme {
        ProductsListScreen(
            state = state,
            onEvent = {},
        )
    }
}

private class ProductsListScreenPreviewParameterProvider : PreviewParameterProvider<ProductsListScreen.State> {
    override val values: Sequence<ProductsListScreen.State> =
        sequenceOf(
            ProductsListScreen.State.Loading(),
            ProductsListScreen.State.Success(
                products =
                    persistentListOf(
                        ProductsListProduct("1", "Nestle", "Nesquik", "379.0", "8.5", "1.8", "81.0"),
                        ProductsListProduct("2", "Coca-Cola", "Cola", "42.0", "0.0", "0.0", "10.6"),
                    ),
            ),
            ProductsListScreen.State.Success(
                searchQuery = "Nes",
                products =
                    persistentListOf(
                        ProductsListProduct("1", "Nestle", "Nesquik", "379.0", "8.5", "1.8", "81.0"),
                    ),
                expandedProductId = "1",
            ),
        )
}
