package com.cyberfeedforward.loyaltycardmanager.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val welcomeMessage: String,
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        HomeUiState(welcomeMessage = "Welcome to LoyaltyCardManager")
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}


