package com.cyberfeedforward.loyaltycardmanager

import android.app.Application
import android.util.Log
import kotlin.system.exitProcess

class LoyaltyCardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Setup global crash handler to trace all possible crashes
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleUncaughtException(thread, throwable)
        }
    }

    private fun handleUncaughtException(thread: Thread, throwable: Throwable) {
        // Log the crash details
        Log.e("LoyaltyCardApp", "CRASH DETECTED on thread ${thread.name}")
        Log.e("LoyaltyCardApp", throwable.stackTraceToString())

        // In a real app, you might send this to a backend like Firebase Crashlytics here.
        // For local tracing, the Log.e output will be captured in Logcat and visible 
        // even in obfuscated builds if you have the mapping.txt.

        // After logging, allow the app to crash or restart as per standard Android behavior
        // exitProcess(1) or let the system handle it
    }
}
