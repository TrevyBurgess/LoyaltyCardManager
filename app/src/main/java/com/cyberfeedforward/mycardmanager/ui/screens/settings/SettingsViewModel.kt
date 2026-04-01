package com.cyberfeedforward.mycardmanager.ui.screens.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class SettingsUiState(
    val displayName: String = "",
    val savedDisplayName: String = "",
    val errorMessage: String? = null,
)

sealed interface SettingsEvent {
    data class OnDisplayNameChanged(val value: String) : SettingsEvent
    data object OnSaveClicked : SettingsEvent
}

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnDisplayNameChanged -> {
                _uiState.update { it.copy(displayName = event.value, errorMessage = null) }
            }
            SettingsEvent.OnSaveClicked -> {
                val name = _uiState.value.displayName.trim()
                if (name.isEmpty()) {
                    _uiState.update { it.copy(errorMessage = "Display name cannot be empty") }
                } else {
                    _uiState.update {
                        it.copy(
                            savedDisplayName = name,
                            displayName = name,
                            errorMessage = null,
                        )
                    }
                }
            }
        }
    }
}
