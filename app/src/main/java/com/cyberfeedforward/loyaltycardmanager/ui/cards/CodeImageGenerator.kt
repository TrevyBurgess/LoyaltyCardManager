package com.cyberfeedforward.loyaltycardmanager.ui.cards

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.common.BitMatrix

object CodeImageGenerator {

    fun generateQrBitmap(
        value: String,
        sizePx: Int,
    ): Bitmap {
        val matrix = CodeMatrixGenerator.generateQrMatrix(
            value = value,
            sizePx = sizePx,
        )
        return bitmapFromMatrix(matrix)
    }

    fun generateBarcodeBitmap(
        value: String,
        widthPx: Int,
        heightPx: Int,
    ): Bitmap {
        val matrix = CodeMatrixGenerator.generateBarcodeMatrix(
            value = value,
            widthPx = widthPx,
            heightPx = heightPx,
        )
        return bitmapFromMatrix(matrix)
    }

    private fun bitmapFromMatrix(matrix: BitMatrix): Bitmap {
        val bitmap = Bitmap.createBitmap(matrix.width, matrix.height, Bitmap.Config.ARGB_8888)
        for (y in 0 until matrix.height) {
            for (x in 0 until matrix.width) {
                bitmap.setPixel(x, y, if (matrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }
}


