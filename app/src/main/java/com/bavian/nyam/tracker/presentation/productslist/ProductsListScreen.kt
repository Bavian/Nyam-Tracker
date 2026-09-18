package com.bavian.nyam.tracker.presentation.productslist

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
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
                    product = product,
                    onClick = {
                        onEvent(ProductsListEvent.ProductClicked(product))
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                    },
                    onMenuClick = { onEvent(ProductsListEvent.ContextMenuClicked(product)) },
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductsListProduct,
    onClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = product.manufacturer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ProductNutrientInfo(
                        label = stringResource(R.string.products_list_calories_prefix),
                        value = product.calories,
                    )
                    ProductNutrientInfo(
                        label = stringResource(R.string.products_list_proteins_prefix),
                        value = product.proteins,
                    )
                    ProductNutrientInfo(
                        label = stringResource(R.string.products_list_fat_prefix),
                        value = product.fat,
                    )
                    ProductNutrientInfo(
                        label = stringResource(R.string.products_list_carbohydrates_prefix),
                        value = product.carbohydrates,
                    )
                }
            }
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.products_list_context_menu_description),
                )
            }
        }
    }
}

@Composable
private fun ProductNutrientInfo(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun ProductsListScreenPreview() {
    ProductsListScreen(
        state =
            ProductsListUiState(
                products =
                    listOf(
                        ProductsListProduct("1", "Nestle", "Nesquik", "379.0", "8.5", "1.8", "81.0"),
                        ProductsListProduct("2", "Coca-Cola", "Cola", "42.0", "0.0", "0.0", "10.6"),
                    ),
            ),
    ) {}
}
