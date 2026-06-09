package com.cyberfeedforward.loyaltycardmanager.ui.cards

sealed interface ScanResultUi {
    data class Success(
        val value: String,
        val type: ScannedCodeType,
    ) : ScanResultUi
    data class Error(val message: String) : ScanResultUi
}


