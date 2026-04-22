package com.cyberfeedforward.loyaltycardmanager2.ui.cards

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.io.path.createTempDirectory

class ScanHistoryStorageTest {

    @Test
    fun append_whenFileDoesNotExist_createsJsonArrayWithEntry() {
        val dir = createTempDirectory("scan-history-").toFile()
        try {
            val file = File(dir, "scanned_codes.json")
            val storage = ScanHistoryStorage(file)

            storage.append(
                ScanHistoryStorage.SavedScan(
                    name = "My Card",
                    code = "12345",
                    type = ScannedCodeType.QrCode,
                )
            )

            val all = storage.readAll()
            assertEquals(1, all.size)
            assertEquals("My Card", all[0].name)
            assertEquals("12345", all[0].code)
            assertEquals(ScannedCodeType.QrCode, all[0].type)
            assertTrue(file.exists())
        } finally {
            dir.deleteRecursively()
        }
    }

    @Test
    fun append_multipleTimes_appendsEntriesInOrder() {
        val dir = createTempDirectory("scan-history-").toFile()
        try {
            val file = File(dir, "scanned_codes.json")
            val storage = ScanHistoryStorage(file)

            storage.append(
                ScanHistoryStorage.SavedScan(
                    name = "A",
                    code = "111",
                    type = ScannedCodeType.Barcode1D,
                )
            )
            storage.append(
                ScanHistoryStorage.SavedScan(
                    name = "B",
                    code = "222",
                    type = ScannedCodeType.QrCode,
                )
            )

            val all = storage.readAll()
            assertEquals(2, all.size)
            assertEquals("A", all[0].name)
            assertEquals("111", all[0].code)
            assertEquals(ScannedCodeType.Barcode1D, all[0].type)
            assertEquals("B", all[1].name)
            assertEquals("222", all[1].code)
            assertEquals(ScannedCodeType.QrCode, all[1].type)
        } finally {
            dir.deleteRecursively()
        }
    }

    @Test
    fun readAll_whenFileContainsInvalidJson_returnsEmptyList() {
        val dir = createTempDirectory("scan-history-").toFile()
        try {
            val file = File(dir, "scanned_codes.json")
            file.writeText("not json")

            val storage = ScanHistoryStorage(file)
            val all = storage.readAll()

            assertEquals(0, all.size)
        } finally {
            dir.deleteRecursively()
        }
    }

    @Test
    fun updateAt_updatesEntryAtIndex_andReturnsTrue() {
        val dir = createTempDirectory("scan-history-").toFile()
        try {
            val file = File(dir, "scanned_codes.json")
            val storage = ScanHistoryStorage(file)

            storage.append(
                ScanHistoryStorage.SavedScan(
                    name = "Old",
                    code = "111",
                    type = ScannedCodeType.Barcode1D,
                )
            )

            val updated = storage.updateAt(
                index = 0,
                scan = ScanHistoryStorage.SavedScan(
                    name = "New",
                    code = "222",
                    type = ScannedCodeType.QrCode,
                ),
            )

            assertTrue(updated)
            val all = storage.readAll()
            assertEquals(1, all.size)
            assertEquals("New", all[0].name)
            assertEquals("222", all[0].code)
            assertEquals(ScannedCodeType.QrCode, all[0].type)
        } finally {
            dir.deleteRecursively()
        }
    }

    @Test
    fun updateAt_whenIndexOutOfBounds_returnsFalseAndDoesNotChangeFile() {
        val dir = createTempDirectory("scan-history-").toFile()
        try {
            val file = File(dir, "scanned_codes.json")
            val storage = ScanHistoryStorage(file)

            storage.append(
                ScanHistoryStorage.SavedScan(
                    name = "A",
                    code = "111",
                    type = ScannedCodeType.Barcode1D,
                )
            )

            val updated = storage.updateAt(
                index = 2,
                scan = ScanHistoryStorage.SavedScan(
                    name = "B",
                    code = "222",
                    type = ScannedCodeType.QrCode,
                ),
            )

            assertEquals(false, updated)
            val all = storage.readAll()
            assertEquals(1, all.size)
            assertEquals("A", all[0].name)
        } finally {
            dir.deleteRecursively()
        }
    }

    @Test
    fun deleteAt_deletesEntryAtIndex_andReturnsTrue() {
        val dir = createTempDirectory("scan-history-").toFile()
        try {
            val file = File(dir, "scanned_codes.json")
            val storage = ScanHistoryStorage(file)

            storage.append(
                ScanHistoryStorage.SavedScan(
                    name = "A",
                    code = "111",
                    type = ScannedCodeType.Barcode1D,
                )
            )
            storage.append(
                ScanHistoryStorage.SavedScan(
                    name = "B",
                    code = "222",
                    type = ScannedCodeType.QrCode,
                )
            )

            val deleted = storage.deleteAt(0)

            assertTrue(deleted)
            val all = storage.readAll()
            assertEquals(1, all.size)
            assertEquals("B", all[0].name)
        } finally {
            dir.deleteRecursively()
        }
    }

    @Test
    fun deleteAt_whenIndexOutOfBounds_returnsFalse() {
        val dir = createTempDirectory("scan-history-").toFile()
        try {
            val file = File(dir, "scanned_codes.json")
            val storage = ScanHistoryStorage(file)

            val deleted = storage.deleteAt(0)

            assertEquals(false, deleted)
        } finally {
            dir.deleteRecursively()
        }
    }

    @Test
    fun readAll_whenRootIsSingleJsonObject_readsSingleEntry() {
        val dir = createTempDirectory("scan-history-").toFile()
        try {
            val file = File(dir, "scanned_codes.json")
            file.writeText(
                """{"name":"Solo","code":"999","type":"QrCode"}"""
            )

            val storage = ScanHistoryStorage(file)
            val all = storage.readAll()

            assertEquals(1, all.size)
            assertEquals("Solo", all[0].name)
            assertEquals("999", all[0].code)
            assertEquals(ScannedCodeType.QrCode, all[0].type)
        } finally {
            dir.deleteRecursively()
        }
    }
}


