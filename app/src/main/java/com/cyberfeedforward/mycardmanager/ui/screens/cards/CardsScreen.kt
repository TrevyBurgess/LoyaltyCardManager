package com.cyberfeedforward.mycardmanager.ui.screens.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CardsRoute(viewModel: CardsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CardsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun CardsScreen(
    uiState: CardsUiState,
    onEvent: (CardsEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "Cards")

        OutlinedTextField(
            value = uiState.newCardName,
            onValueChange = { onEvent(CardsEvent.OnNewCardNameChanged(it)) },
            label = { Text(text = "New card") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(onClick = { onEvent(CardsEvent.OnAddCardClicked) }) {
                Text(text = "Add")
            }
            uiState.errorMessage?.let { Text(text = it) }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(uiState.cards) { index, card ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = card)
                    Button(onClick = { onEvent(CardsEvent.OnRemoveCardClicked(index)) }) {
                        Text(text = "Remove")
                    }
                }
            }
        }
    }
}
