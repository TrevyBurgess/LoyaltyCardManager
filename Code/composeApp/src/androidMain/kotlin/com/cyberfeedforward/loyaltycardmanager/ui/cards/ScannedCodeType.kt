package com.cyberfeedforward.loyaltycardmanager.ui.cards

enum class ScannedCodeType(
    val label: String,
    val isQr: Boolean,
) {
    QrCode(label = "QR Code", isQr = true),
    Barcode1D(label = "1D Barcode", isQr = false),
}


