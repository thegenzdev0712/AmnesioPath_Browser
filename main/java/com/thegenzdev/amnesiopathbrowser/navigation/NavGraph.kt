package com.thegenzdev.amnesiopathbrowser.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thegenzdev.amnesiopathbrowser.screens.MainScreen
import com.thegenzdev.amnesiopathbrowser.screens.SettingsScreen

object Routes {
    const val MAIN = "main"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavHost(
    startRoute: String = Routes.MAIN,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = startRoute) {
        composable(Routes.MAIN) {
            MainScreen(
                onSettingsClick = { navController.navigate(Routes.SETTINGS) }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}