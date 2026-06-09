package com.cyberfeedforward.loyaltycardmanager.util

import android.util.Log

object Logger {
    private const val TAG = "LoyaltyCardApp"

    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }

    fun w(message: String, throwable: Throwable? = null) {
        Log.w(TAG, message, throwable)
    }

    fun d(message: String) {
        Log.d(TAG, message)
    }

    fun i(message: String) {
        Log.i(TAG, message)
    }
}
