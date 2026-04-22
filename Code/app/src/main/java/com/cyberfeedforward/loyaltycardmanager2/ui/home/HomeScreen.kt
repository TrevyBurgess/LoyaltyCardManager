package com.cyberfeedforward.loyaltycardmanager2.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cyberfeedforward.loyaltycardmanager2.ui.theme.LoyaltyCardManagerTheme

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = uiState.welcomeMessage,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    LoyaltyCardManagerTheme {
        HomeScreen(
            uiState = HomeUiState(welcomeMessage = "Welcome to LoyaltyCardManager"),
        )
    }
}


