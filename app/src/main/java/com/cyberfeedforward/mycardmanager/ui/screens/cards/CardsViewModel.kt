package com.cyberfeedforward.mycardmanager.ui.screens.cards

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class CardsUiState(
    val cards: List<String> = emptyList(),
    val newCardName: String = "",
    val errorMessage: String? = null,
)

sealed interface CardsEvent {
    data class OnNewCardNameChanged(val value: String) : CardsEvent
    data object OnAddCardClicked : CardsEvent
    data class OnRemoveCardClicked(val index: Int) : CardsEvent
}

class CardsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CardsUiState())
    val uiState: StateFlow<CardsUiState> = _uiState

    fun onEvent(event: CardsEvent) {
        when (event) {
            is CardsEvent.OnNewCardNameChanged -> {
                _uiState.update { it.copy(newCardName = event.value, errorMessage = null) }
            }
            CardsEvent.OnAddCardClicked -> {
                val name = _uiState.value.newCardName.trim()
                if (name.isEmpty()) {
                    _uiState.update { it.copy(errorMessage = "Card name cannot be empty") }
                    return
                }

                _uiState.update { current ->
                    current.copy(
                        cards = current.cards + name,
                        newCardName = "",
                        errorMessage = null,
                    )
                }
            }
            is CardsEvent.OnRemoveCardClicked -> {
                _uiState.update { current ->
                    if (event.index !in current.cards.indices) {
                        current.copy(errorMessage = "Invalid card index")
                    } else {
                        current.copy(
                            cards = current.cards.toMutableList().also { it.removeAt(event.index) },
                            errorMessage = null,
                        )
                    }
                }
            }
        }
    }
}
