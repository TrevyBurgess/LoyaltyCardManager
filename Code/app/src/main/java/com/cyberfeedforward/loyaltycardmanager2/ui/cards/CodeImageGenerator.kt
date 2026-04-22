package com.cyberfeedforward.loyaltycardmanager2.ui.cards

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.common.BitMatrix
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

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
        val bitmap = createBitmap(matrix.width, matrix.height)
        for (y in 0 until matrix.height) {
            for (x in 0 until matrix.width) {
                bitmap[x, y] = if (matrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        return bitmap
    }
}


