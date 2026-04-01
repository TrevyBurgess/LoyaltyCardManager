package com.cyberfeedforward.mycardmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cyberfeedforward.mycardmanager.ui.screens.cards.CardsRoute
import com.cyberfeedforward.mycardmanager.ui.screens.home.HomeRoute
import com.cyberfeedforward.mycardmanager.ui.screens.settings.SettingsRoute

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = AppDestination.Home.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(AppDestination.Home.route) {
            HomeRoute(viewModel = viewModel())
        }
        composable(AppDestination.Cards.route) {
            CardsRoute(viewModel = viewModel())
        }
        composable(AppDestination.Settings.route) {
            SettingsRoute(viewModel = viewModel())
        }
    }
}
