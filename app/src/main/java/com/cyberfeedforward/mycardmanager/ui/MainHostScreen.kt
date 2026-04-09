package com.cyberfeedforward.mycardmanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberfeedforward.mycardmanager.ui.navigation.MyCardManagerNavHost
import com.cyberfeedforward.mycardmanager.ui.navigation.TopLevelDestination
import com.cyberfeedforward.mycardmanager.ui.theme.MyCardManagerTheme

@Composable
fun MainHostScreen(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                val bottomBarDestinations = listOf(
                    TopLevelDestination.Cards,
                    TopLevelDestination.About,
                )
                bottomBarDestinations.forEach { destination ->
                    val selected = currentRoute == destination.route
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
                            NavigationIcon(
                                icon = if (selected) destination.selectedIcon else destination.unselectedIcon,
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "My Card Manager",
                    fontSize = 36.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    )
            }
            
            MyCardManagerNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun NavigationIcon(
    icon: ImageVector,
    contentDescription: String,
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
    )
}

@Preview(showBackground = true)
@Composable
private fun MainHostScreenPreview() {
    MyCardManagerTheme {
        MainHostScreen()
    }
}
