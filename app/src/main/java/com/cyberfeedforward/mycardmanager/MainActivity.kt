package com.cyberfeedforward.mycardmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cyberfeedforward.mycardmanager.ui.host.AppHost
import com.cyberfeedforward.mycardmanager.ui.theme.MyCardManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyCardManagerTheme {
                AppHost()
            }
        }
    }
}