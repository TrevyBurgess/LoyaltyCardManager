package com.cyberfeedforward.loyaltycardmanager

import com.cyberfeedforward.loyaltycardmanager.ui.settings.SettingsViewModel
import com.cyberfeedforward.loyaltycardmanager.ui.settings.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsViewModelTest {

    @Test
    fun initialState_themeModeSystem() {
        val viewModel = SettingsViewModel()
        assertEquals(ThemeMode.System, viewModel.uiState.value.themeMode)
    }

    @Test
    fun onThemeModeChanged_updatesValue() {
        val viewModel = SettingsViewModel()

        viewModel.onThemeModeChanged(ThemeMode.Dark)
        assertEquals(ThemeMode.Dark, viewModel.uiState.value.themeMode)

        viewModel.onThemeModeChanged(ThemeMode.Light)
        assertEquals(ThemeMode.Light, viewModel.uiState.value.themeMode)
    }
}


