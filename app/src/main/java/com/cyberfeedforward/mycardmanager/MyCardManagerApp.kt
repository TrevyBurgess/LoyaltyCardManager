package com.cyberfeedforward.mycardmanager

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cyberfeedforward.mycardmanager.navigation.AppBottomBar
import com.cyberfeedforward.mycardmanager.navigation.AppNavGraph

@Composable
fun MyCardManagerApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            contentPadding = innerPadding
        )
    }
}
