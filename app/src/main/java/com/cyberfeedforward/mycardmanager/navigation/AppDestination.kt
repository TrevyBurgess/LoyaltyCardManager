package com.cyberfeedforward.mycardmanager.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : AppDestination(
        route = "home",
        label = "Home",
        icon = Icons.Filled.Home
    )

    data object Cards : AppDestination(
        route = "cards",
        label = "Cards",
        icon = Icons.Filled.CreditCard
    )

    data object Settings : AppDestination(
        route = "settings",
        label = "Settings",
        icon = Icons.Filled.Settings
    )
}

val BottomNavDestinations: List<AppDestination> = listOf(
    AppDestination.Home,
    AppDestination.Cards,
    AppDestination.Settings
)
