package com.cyberfeedforward.loyaltycardmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyberfeedforward.loyaltycardmanager.ui.MainHostScreen
import com.cyberfeedforward.loyaltycardmanager.ui.settings.SettingsViewModel
import com.cyberfeedforward.loyaltycardmanager.ui.theme.LoyaltyCardManagerTheme

class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

            LoyaltyCardManagerTheme(themeMode = settingsUiState.themeMode) {
                MainHostScreen(
                    settingsViewModel = settingsViewModel,
                    modifier = Modifier
                )
            }
        }
    }
}
