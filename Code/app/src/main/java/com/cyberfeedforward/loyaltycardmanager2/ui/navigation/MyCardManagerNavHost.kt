package com.cyberfeedforward.loyaltycardmanager2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cyberfeedforward.loyaltycardmanager2.ui.about.AboutRoute
import com.cyberfeedforward.loyaltycardmanager2.ui.cards.CardsRoute
import com.cyberfeedforward.loyaltycardmanager2.ui.home.HomeRoute
import com.cyberfeedforward.loyaltycardmanager2.ui.settings.SettingsRoute

@Composable
fun LoyaltyCardManagerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TopLevelDestination.Cards.route,
        modifier = modifier,
    ) {
        composable(route = TopLevelDestination.Home.route) {
            HomeRoute()
        }
        composable(route = TopLevelDestination.Cards.route) {
            CardsRoute()
        }
        composable(route = TopLevelDestination.Settings.route) {
            SettingsRoute()
        }
        composable(route = TopLevelDestination.About.route) {
            AboutRoute()
        }
    }
}

