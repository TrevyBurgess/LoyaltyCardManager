package com.cyberfeedforward.mycardmanager.ui.host

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cyberfeedforward.mycardmanager.navigation.AppDestination
import com.cyberfeedforward.mycardmanager.navigation.AppNavGraph

@Composable
fun AppHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavDestinations = listOf(
        AppDestination.Home,
        AppDestination.Cards,
        AppDestination.Settings,
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                bottomNavDestinations.forEach { destination ->
                    val selected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == destination.route }
                        ?: false

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label,
                            )
                        },
                        label = {
                            Text(text = destination.label)
                        },
                    )
                }
            }
        },
    ) { innerPadding: PaddingValues ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}
