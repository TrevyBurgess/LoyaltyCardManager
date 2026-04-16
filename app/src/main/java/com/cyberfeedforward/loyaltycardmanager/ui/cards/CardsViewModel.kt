package com.cyberfeedforward.loyaltycardmanager.ui.cards

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CardsUiState(
    val cardCount: Int,
    val isScannerVisible: Boolean,
    val scanResult: ScanResultUi?,
)

class CardsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        CardsUiState(
            cardCount = 0,
            isScannerVisible = false,
            scanResult = null,
        )
    )
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()

    fun onAddCard() {
        _uiState.value = _uiState.value.copy(cardCount = _uiState.value.cardCount + 1)
    }

    fun onRemoveCard() {
        val current = _uiState.value.cardCount
        _uiState.value = _uiState.value.copy(cardCount = (current - 1).coerceAtLeast(0))
    }

    fun onScanRequested() {
        _uiState.value = _uiState.value.copy(
            isScannerVisible = true,
            scanResult = null,
        )
    }

    fun onScannerDismissed() {
        _uiState.value = _uiState.value.copy(isScannerVisible = false)
    }

    fun onBarcodeScanned(
        value: String,
        type: ScannedCodeType,
    ) {
        _uiState.value = _uiState.value.copy(
            isScannerVisible = false,
            scanResult = ScanResultUi.Success(value = value, type = type),
        )
    }

    fun onScanError(message: String) {
        _uiState.value = _uiState.value.copy(
            isScannerVisible = false,
            scanResult = ScanResultUi.Error(message = message),
        )
    }

    fun onScanResultDismissed() {
        _uiState.value = _uiState.value.copy(scanResult = null)
    }
}


