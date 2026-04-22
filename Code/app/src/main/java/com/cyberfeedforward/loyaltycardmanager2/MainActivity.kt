package com.cyberfeedforward.loyaltycardmanager2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.cyberfeedforward.loyaltycardmanager2.ui.MainHostScreen
import com.cyberfeedforward.loyaltycardmanager2.ui.theme.LoyaltyCardManagerTheme

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
