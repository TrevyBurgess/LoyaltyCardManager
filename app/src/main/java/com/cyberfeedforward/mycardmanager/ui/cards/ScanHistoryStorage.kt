package com.cyberfeedforward.mycardmanager.ui.cards

import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class ScanHistoryStorage(
    private val file: File,
) {
    data class SavedScan(
        val name: String,
        val code: String,
        val type: ScannedCodeType,
    )

    fun append(scan: SavedScan) {
        val array = readArrayOrEmpty()
        array.put(
            JSONObject()
                .put("name", scan.name)
                .put("code", scan.code)
                .put("type", scan.type.name)
        )
        writeArray(array)
    }

    fun updateAt(
        index: Int,
        scan: SavedScan,
    ): Boolean {
        if (index < 0) return false

        val array = readArrayOrEmpty()
        if (index >= array.length()) return false

        val obj = JSONObject()
            .put("name", scan.name)
            .put("code", scan.code)
            .put("type", scan.type.name)

        array.put(index, obj)
        writeArray(array)
        return true
    }

    fun deleteAt(index: Int): Boolean {
        if (index < 0) return false

        val array = readArrayOrEmpty()
        if (index >= array.length()) return false

        array.remove(index)
        writeArray(array)
        return true
    }

    fun readAll(): List<SavedScan> {
        val array = readArrayOrEmpty()
        return buildList(array.length()) {
            for (i in 0 until array.length()) {
                val obj = array.optJSONObject(i) ?: continue
                val name = obj.optString("name", "")
                val code = obj.optString("code", "")
                val typeName = obj.optString("type", ScannedCodeType.Barcode1D.name)
                val type = ScannedCodeType.entries.firstOrNull { it.name == typeName }
                    ?: ScannedCodeType.Barcode1D
                add(SavedScan(name = name, code = code, type = type))
            }
        }
    }

    private fun readArrayOrEmpty(): JSONArray {
        if (!file.exists()) return JSONArray()

        return try {
            val content = file.readText(Charsets.UTF_8).trim()
            if (content.isEmpty()) {
                JSONArray()
            } else if (content.startsWith("{")) {
                JSONArray().put(JSONObject(content))
            } else {
                JSONArray(content)
            }
        } catch (_: Exception) {
            JSONArray()
        }
    }

    private fun writeArray(array: JSONArray) {
        file.parentFile?.mkdirs()
        file.writeText(array.toString(), Charsets.UTF_8)
    }
}
