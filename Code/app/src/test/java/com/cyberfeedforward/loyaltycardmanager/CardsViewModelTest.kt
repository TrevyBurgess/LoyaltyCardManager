package com.cyberfeedforward.loyaltycardmanager

import com.cyberfeedforward.loyaltycardmanager.ui.cards.CardsViewModel
import com.cyberfeedforward.loyaltycardmanager.ui.cards.ScanResultUi
import com.cyberfeedforward.loyaltycardmanager.ui.cards.ScannedCodeType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CardsViewModelTest {

    @Test
    fun initialState_isEmpty() {
        val viewModel = CardsViewModel()
        assertTrue(viewModel.uiState.value.savedScans.isEmpty())
        assertFalse(viewModel.uiState.value.isScannerVisible)
        assertNull(viewModel.uiState.value.scanResult)
    }

    @Test
    fun onScanRequested_opensScannerAndClearsPreviousResult() {
        val viewModel = CardsViewModel()

        viewModel.onScanError("Previous error")
        viewModel.onScanRequested()

        assertTrue(viewModel.uiState.value.isScannerVisible)
        assertNull(viewModel.uiState.value.scanResult)
    }

    @Test
    fun onScannerDismissed_closesScanner() {
        val viewModel = CardsViewModel()

        viewModel.onScanRequested()
        viewModel.onScannerDismissed()

        assertFalse(viewModel.uiState.value.isScannerVisible)
    }

    @Test
    fun onBarcodeScanned_closesScannerAndSetsSuccessResult() {
        val viewModel = CardsViewModel()

        viewModel.onScanRequested()
        viewModel.onBarcodeScanned("12345", ScannedCodeType.QrCode)

        assertFalse(viewModel.uiState.value.isScannerVisible)
        val result = viewModel.uiState.value.scanResult
        assertTrue(result is ScanResultUi.Success)
        assertEquals("12345", (result as ScanResultUi.Success).value)
        assertEquals(ScannedCodeType.QrCode, result.type)
    }

    @Test
    fun onScanError_closesScannerAndSetsErrorResult() {
        val viewModel = CardsViewModel()

        viewModel.onScanRequested()
        viewModel.onScanError("No camera")

        assertFalse(viewModel.uiState.value.isScannerVisible)
        val result = viewModel.uiState.value.scanResult
        assertTrue(result is ScanResultUi.Error)
        assertEquals("No camera", (result as ScanResultUi.Error).message)
    }

    @Test
    fun onScanResultDismissed_clearsResult() {
        val viewModel = CardsViewModel()

        viewModel.onBarcodeScanned("abc", ScannedCodeType.Barcode1D)
        viewModel.onScanResultDismissed()

        assertNull(viewModel.uiState.value.scanResult)
    }
}
