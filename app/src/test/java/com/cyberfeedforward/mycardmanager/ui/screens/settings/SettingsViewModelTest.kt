package com.cyberfeedforward.mycardmanager.ui.screens.settings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class SettingsViewModelTest {

    @Test
    fun `save with valid name persists and clears error`() {
        val viewModel = SettingsViewModel()

        viewModel.onEvent(SettingsEvent.OnDisplayNameChanged("Ash"))
        viewModel.onEvent(SettingsEvent.OnSaveClicked)

        assertEquals("Ash", viewModel.uiState.value.savedDisplayName)
        assertEquals("Ash", viewModel.uiState.value.displayName)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `save with blank name sets error (negative case)`() {
        val viewModel = SettingsViewModel()

        viewModel.onEvent(SettingsEvent.OnDisplayNameChanged("  "))
        viewModel.onEvent(SettingsEvent.OnSaveClicked)

        assertEquals("", viewModel.uiState.value.savedDisplayName)
        assertNotNull(viewModel.uiState.value.errorMessage)
    }
}
