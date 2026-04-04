package com.cyberfeedforward.mycardmanager.ui.cards

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.json.JSONArray
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ScanHistoryStorageInstrumentedTest {

    @Test
    fun append_persistsRequiredFields_andReadAllReturnsThem() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File(context.filesDir, "scanned_codes.json")
        file.delete()

        val storage = ScanHistoryStorage(file)

        val first = ScanHistoryStorage.SavedScan(
            name = "Costco",
            code = "1234567890",
            type = ScannedCodeType.Barcode1D,
        )
        val second = ScanHistoryStorage.SavedScan(
            name = "My QR",
            code = "https://example.com",
            type = ScannedCodeType.QrCode,
        )

        storage.append(first)
        storage.append(second)

        assertTrue(file.exists())

        val json = JSONArray(file.readText(Charsets.UTF_8))
        assertEquals(2, json.length())

        val jsonFirst = json.getJSONObject(0)
        assertEquals("Costco", jsonFirst.getString("name"))
        assertEquals("1234567890", jsonFirst.getString("code"))
        assertEquals("Barcode1D", jsonFirst.getString("type"))

        val jsonSecond = json.getJSONObject(1)
        assertEquals("My QR", jsonSecond.getString("name"))
        assertEquals("https://example.com", jsonSecond.getString("code"))
        assertEquals("QrCode", jsonSecond.getString("type"))

        val all = storage.readAll()
        assertEquals(listOf(first, second), all)

        file.delete()
    }
}
