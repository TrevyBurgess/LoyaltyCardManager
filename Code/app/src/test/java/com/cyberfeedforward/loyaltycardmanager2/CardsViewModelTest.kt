package com.cyberfeedforward.loyaltycardmanager2

import com.cyberfeedforward.loyaltycardmanager2.ui.cards.CardsViewModel
import com.cyberfeedforward.loyaltycardmanager2.ui.cards.ScanResultUi
import com.cyberfeedforward.loyaltycardmanager2.ui.cards.ScannedCodeType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CardsViewModelTest {

    @Test
    fun initialState_cardCountIsZero() {
        val viewModel = CardsViewModel()
        assertEquals(0, viewModel.uiState.value.cardCount)
        assertFalse(viewModel.uiState.value.isScannerVisible)
        assertNull(viewModel.uiState.value.scanResult)
    }

    @Test
    fun onAddCard_incrementsCount() {
        val viewModel = CardsViewModel()

        viewModel.onAddCard()
        viewModel.onAddCard()

        assertEquals(2, viewModel.uiState.value.cardCount)
    }

    @Test
    fun onRemoveCard_whenZero_doesNotGoNegative() {
        val viewModel = CardsViewModel()

        viewModel.onRemoveCard()

        assertEquals(0, viewModel.uiState.value.cardCount)
    }

    @Test
    fun onRemoveCard_whenPositive_decrementsCount() {
        val viewModel = CardsViewModel()

        viewModel.onAddCard()
        viewModel.onAddCard()
        viewModel.onRemoveCard()

        assertEquals(1, viewModel.uiState.value.cardCount)
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


