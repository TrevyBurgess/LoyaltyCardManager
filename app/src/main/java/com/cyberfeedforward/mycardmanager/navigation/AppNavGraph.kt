package com.cyberfeedforward.mycardmanager.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cyberfeedforward.mycardmanager.screens.cards.CardsRoute
import com.cyberfeedforward.mycardmanager.screens.home.HomeRoute
import com.cyberfeedforward.mycardmanager.screens.settings.SettingsRoute

@Composable
fun AppNavGraph(
    navController: NavHostController,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Home.route
    ) {
        composable(AppDestination.Home.route) {
            HomeRoute(contentPadding = contentPadding)
        }

        composable(AppDestination.Cards.route) {
            CardsRoute(contentPadding = contentPadding)
        }

        composable(AppDestination.Settings.route) {
            SettingsRoute(contentPadding = contentPadding)
        }
    }
}
