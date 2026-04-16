package com.cyberfeedforward.loyaltycardmanager.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    val darkModeEnabled: Boolean,
)

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState(darkModeEnabled = false))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun onToggleDarkMode() {
        _uiState.value = _uiState.value.copy(darkModeEnabled = !_uiState.value.darkModeEnabled)
    }
}


