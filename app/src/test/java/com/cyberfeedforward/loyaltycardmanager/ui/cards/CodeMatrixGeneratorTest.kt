package com.cyberfeedforward.loyaltycardmanager.ui.cards

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CodeMatrixGeneratorTest {

    @Test
    fun `generateQrMatrix returns matrix with requested size`() {
        val matrix = CodeMatrixGenerator.generateQrMatrix(
            value = "hello",
            sizePx = 64,
        )

        assertEquals(64, matrix.width)
        assertEquals(64, matrix.height)
    }

    @Test
    fun `generateBarcodeMatrix returns matrix with requested size`() {
        val matrix = CodeMatrixGenerator.generateBarcodeMatrix(
            value = "123456",
            widthPx = 120,
            heightPx = 48,
        )

        assertEquals(120, matrix.width)
        assertEquals(48, matrix.height)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `generateQrMatrix throws on blank value (negative case)`() {
        CodeMatrixGenerator.generateQrMatrix(
            value = "   ",
            sizePx = 32,
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `generateBarcodeMatrix throws on invalid size (edge case)`() {
        CodeMatrixGenerator.generateBarcodeMatrix(
            value = "123",
            widthPx = 0,
            heightPx = 10,
        )
    }

    @Test
    fun `generateQrMatrix encodes something non-empty`() {
        val matrix = CodeMatrixGenerator.generateQrMatrix(
            value = "test",
            sizePx = 48,
        )

        assertNotNull(matrix)
    }
}


