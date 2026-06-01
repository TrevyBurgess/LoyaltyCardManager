package com.cyberfeedforward.loyaltycardmanager.ui.cards

import androidx.lifecycle.ViewModel
import com.cyberfeedforward.loyaltycardmanager.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CardsUiState(
    val savedScans: List<ScanHistoryStorage.SavedScan> = emptyList(),
    val isScannerVisible: Boolean = false,
    val scanResult: ScanResultUi? = null,
    val viewingIndex: Int? = null,
    val editingIndex: Int? = null,
    val pendingDeleteIndex: Int? = null,
)

class CardsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CardsUiState())
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()

    private var storage: ScanHistoryStorage? = null

    fun initStorage(storage: ScanHistoryStorage) {
        if (this.storage != null) return
        this.storage = storage
        loadScans()
    }

    private fun loadScans() {
        _uiState.value = _uiState.value.copy(
            savedScans = storage?.readAll() ?: emptyList(),
        )
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
        Logger.e("Scan error: $message")
        _uiState.value = _uiState.value.copy(
            isScannerVisible = false,
            scanResult = ScanResultUi.Error(message = message),
        )
    }

    fun onScanResultDismissed() {
        _uiState.value = _uiState.value.copy(scanResult = null)
    }

    fun onCardClick(index: Int) {
        _uiState.value = _uiState.value.copy(viewingIndex = index)
    }

    fun onDismissView() {
        _uiState.value = _uiState.value.copy(viewingIndex = null)
    }

    fun onEditRequested(index: Int) {
        _uiState.value = _uiState.value.copy(editingIndex = index)
    }

    fun onDismissEdit() {
        _uiState.value = _uiState.value.copy(editingIndex = null)
    }

    fun onDeleteRequested(index: Int) {
        _uiState.value = _uiState.value.copy(pendingDeleteIndex = index)
    }

    fun onDismissDelete() {
        _uiState.value = _uiState.value.copy(pendingDeleteIndex = null)
    }

    fun onConfirmDelete() {
        val index = _uiState.value.pendingDeleteIndex ?: return
        Logger.i("Deleting card at index $index")
        if (storage?.deleteAt(index) == true) {
            loadScans()
        } else {
            Logger.e("Failed to delete card at index $index")
        }
        _uiState.value = _uiState.value.copy(pendingDeleteIndex = null)
    }

    fun onSaveEdit(index: Int, scan: ScanHistoryStorage.SavedScan) {
        Logger.i("Saving edit for card at index $index")
        if (storage?.updateAt(index, scan) == true) {
            loadScans()
        } else {
            Logger.e("Failed to update card at index $index")
        }
        _uiState.value = _uiState.value.copy(editingIndex = null)
    }

    fun onSaveNewScan(scan: ScanHistoryStorage.SavedScan) {
        Logger.i("Saving new scanned card: ${scan.name}")
        storage?.append(scan)
        loadScans()
        onScanResultDismissed()
    }
}


