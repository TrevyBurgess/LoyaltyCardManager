package com.cyberfeedforward.loyaltycardmanager2.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberfeedforward.loyaltycardmanager2.ui.theme.LoyaltyCardManagerTheme

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall,
        )

        Row(
            modifier = Modifier.padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(text = "Dark mode")
            Switch(
                checked = uiState.darkModeEnabled,
                onCheckedChange = { onToggleDarkMode() },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    LoyaltyCardManagerTheme {
        SettingsScreen(
            uiState = SettingsUiState(darkModeEnabled = true),
            onToggleDarkMode = {},
        )
    }
}


