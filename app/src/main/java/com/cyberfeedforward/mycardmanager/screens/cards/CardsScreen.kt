package com.cyberfeedforward.mycardmanager.screens.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CardsRoute(
    contentPadding: PaddingValues,
    viewModel: CardsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CardsScreen(
        uiState = uiState,
        contentPadding = contentPadding
    )
}

@Composable
fun CardsScreen(
    uiState: CardsUiState,
    contentPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Cards",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "You have ${uiState.count} cards.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
