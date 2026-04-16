package com.cyberfeedforward.loyaltycardmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.cyberfeedforward.loyaltycardmanager.ui.MainHostScreen
import com.cyberfeedforward.loyaltycardmanager.ui.theme.LoyaltyCardManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoyaltyCardManagerTheme {
                MainHostScreen(modifier = Modifier)
            }
        }
    }
}
