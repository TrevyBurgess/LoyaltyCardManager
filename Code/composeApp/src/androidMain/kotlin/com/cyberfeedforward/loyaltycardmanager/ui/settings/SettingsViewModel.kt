package com.cyberfeedforward.loyaltycardmanager.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cyberfeedforward.loyaltycardmanager.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    Light,
    Dark,
    System
}

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.System,
    val removeControlCharacters: Boolean = true,
)

class SettingsViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SettingsUiState(
            themeMode = runCatching {
                val modeName = sharedPreferences.getString("theme_mode", ThemeMode.System.name)
                ThemeMode.valueOf(modeName ?: ThemeMode.System.name)
            }.onFailure { Logger.e("Failed to load theme mode", it) }
                .getOrDefault(ThemeMode.System),
            removeControlCharacters = sharedPreferences.getBoolean("remove_control_characters", true)
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun onThemeModeChanged(mode: ThemeMode) {
        sharedPreferences.edit().putString("theme_mode", mode.name).apply()
        _uiState.value = _uiState.value.copy(themeMode = mode)
    }

    fun onRemoveControlCharactersChanged(remove: Boolean) {
        sharedPreferences.edit().putBoolean("remove_control_characters", remove).apply()
        _uiState.value = _uiState.value.copy(removeControlCharacters = remove)
    }

    class Factory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(sharedPreferences) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


