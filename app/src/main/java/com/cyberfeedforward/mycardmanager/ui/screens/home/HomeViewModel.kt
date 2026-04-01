package com.cyberfeedforward.mycardmanager.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    val greeting: String = "Welcome",
    val counter: Int = 0,
)

sealed interface HomeEvent {
    data object OnIncrementClicked : HomeEvent
    data object OnDecrementClicked : HomeEvent
}

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnIncrementClicked -> {
                _uiState.update { it.copy(counter = it.counter + 1) }
            }
            HomeEvent.OnDecrementClicked -> {
                _uiState.update { current ->
                    current.copy(counter = (current.counter - 1).coerceAtLeast(0))
                }
            }
        }
    }
}
