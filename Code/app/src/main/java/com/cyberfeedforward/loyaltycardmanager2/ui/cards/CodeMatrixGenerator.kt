package com.cyberfeedforward.loyaltycardmanager2.ui.cards

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

object CodeMatrixGenerator {

    fun generateQrMatrix(
        value: String,
        sizePx: Int,
    ): BitMatrix {
        return generateMatrix(
            value = value,
            format = BarcodeFormat.QR_CODE,
            widthPx = sizePx,
            heightPx = sizePx,
        )
    }

    fun generateBarcodeMatrix(
        value: String,
        widthPx: Int,
        heightPx: Int,
    ): BitMatrix {
        return generateMatrix(
            value = value,
            format = BarcodeFormat.CODE_128,
            widthPx = widthPx,
            heightPx = heightPx,
        )
    }

    private fun generateMatrix(
        value: String,
        format: BarcodeFormat,
        widthPx: Int,
        heightPx: Int,
    ): BitMatrix {
        require(value.isNotBlank()) { "value must not be blank" }
        require(widthPx > 0) { "widthPx must be > 0" }
        require(heightPx > 0) { "heightPx must be > 0" }

        return MultiFormatWriter().encode(
            value,
            format,
            widthPx,
            heightPx,
        )
    }
}


