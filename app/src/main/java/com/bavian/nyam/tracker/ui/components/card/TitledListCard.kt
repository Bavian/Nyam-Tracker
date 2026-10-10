package com.bavian.nyam.tracker.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bavian.nyam.tracker.R
import com.bavian.nyam.tracker.ui.model.StringSource
import com.bavian.nyam.tracker.ui.model.stringSource
import com.bavian.nyam.tracker.ui.theme.NyamTrackerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

object TitledListCard {
    data class Item(
        val id: String,
        val text: StringSource,
    )

    data class State(
        val title: StringSource,
        val items: ImmutableList<Item>,
    )

    sealed interface Event {
        data object PlusClicked : Event

        data class ItemCrossClicked(
            val id: String,
        ) : Event
    }
}

@Composable
fun TitledListCard(
    state: TitledListCard.State,
    onEvent: (TitledListCard.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = state.title.get(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(
                    onClick = { onEvent(TitledListCard.Event.PlusClicked) },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_24dp_add_2),
                        contentDescription = null,
                    )
                }
            }

            HorizontalDivider()

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                state.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = item.text.get(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                        IconButton(
                            onClick = { onEvent(TitledListCard.Event.ItemCrossClicked(item.id)) },
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_24dp_close),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun TitledListCardPreview(
    @PreviewParameter(TitledListCardPreviewParameterProvider::class)
    state: TitledListCard.State,
) {
    NyamTrackerTheme {
        TitledListCard(
            state = state,
            onEvent = {},
        )
    }
}

private class TitledListCardPreviewParameterProvider : PreviewParameterProvider<TitledListCard.State> {
    override val values: Sequence<TitledListCard.State> =
        sequenceOf(
            TitledListCard.State(
                title = StringSource.Constant("Ingredients"),
                items =
                    persistentListOf(
                        TitledListCard.Item("1", "Flour".stringSource),
                        TitledListCard.Item("2", "Sugar".stringSource),
                        TitledListCard.Item("3", "Milk".stringSource),
                    ),
            ),
            TitledListCard.State(
                title = StringSource.Constant("Barcodes"),
                items =
                    persistentListOf(
                        TitledListCard.Item("101", "4601234567890".stringSource),
                    ),
            ),
        )
}
