package com.cyberfeedforward.loyaltycardmanager

import android.content.SharedPreferences
import com.cyberfeedforward.loyaltycardmanager.ui.settings.SettingsViewModel
import com.cyberfeedforward.loyaltycardmanager.ui.settings.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class SettingsViewModelTest {

    private val editor: SharedPreferences.Editor = mock {
        on { putString(any(), any()) } doReturn it
    }
    private val sharedPreferences: SharedPreferences = mock {
        on { edit() } doReturn editor
        on { getString(any(), any()) } doReturn ThemeMode.System.name
    }

    @Test
    fun initialState_themeModeSystem() {
        val viewModel = SettingsViewModel(sharedPreferences)
        assertEquals(ThemeMode.System, viewModel.uiState.value.themeMode)
    }

    @Test
    fun onThemeModeChanged_updatesValue() {
        val viewModel = SettingsViewModel(sharedPreferences)

        viewModel.onThemeModeChanged(ThemeMode.Dark)
        assertEquals(ThemeMode.Dark, viewModel.uiState.value.themeMode)

        viewModel.onThemeModeChanged(ThemeMode.Light)
        assertEquals(ThemeMode.Light, viewModel.uiState.value.themeMode)
    }
}
