package com.cyberfeedforward.mycardmanager.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsRoute(viewModel: SettingsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "Settings")

        OutlinedTextField(
            value = uiState.displayName,
            onValueChange = { onEvent(SettingsEvent.OnDisplayNameChanged(it)) },
            label = { Text(text = "Display name") },
            singleLine = true,
        )

        Button(onClick = { onEvent(SettingsEvent.OnSaveClicked) }) {
            Text(text = "Save")
        }

        uiState.errorMessage?.let { Text(text = it) }

        if (uiState.savedDisplayName.isNotBlank()) {
            Text(text = "Saved as: ${uiState.savedDisplayName}")
        }
    }
}
