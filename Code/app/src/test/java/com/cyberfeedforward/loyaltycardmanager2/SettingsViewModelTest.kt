package com.cyberfeedforward.loyaltycardmanager2

import com.cyberfeedforward.loyaltycardmanager2.ui.settings.SettingsViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsViewModelTest {

    @Test
    fun initialState_darkModeDisabled() {
        val viewModel = SettingsViewModel()
        assertFalse(viewModel.uiState.value.darkModeEnabled)
    }

    @Test
    fun onToggleDarkMode_togglesValue() {
        val viewModel = SettingsViewModel()

        viewModel.onToggleDarkMode()
        assertTrue(viewModel.uiState.value.darkModeEnabled)

        viewModel.onToggleDarkMode()
        assertFalse(viewModel.uiState.value.darkModeEnabled)
    }
}


