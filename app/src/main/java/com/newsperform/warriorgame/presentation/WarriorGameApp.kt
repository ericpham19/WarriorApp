package com.newsperform.warriorgame.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.newsperform.warriorgame.domain.model.WarriorType
import com.newsperform.warriorgame.presentation.navigation.ScreenNavigation
import com.newsperform.warriorgame.presentation.screens.AddScreen
import com.newsperform.warriorgame.presentation.screens.HomeScreen
import com.newsperform.warriorgame.presentation.screens.WarriorListScreen

@Composable
fun WarriorGameApp(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = ScreenNavigation.HomeScreen.route,
        modifier = modifier
    ) {
        composable(ScreenNavigation.HomeScreen.route) {
            HomeScreen(navController) { }
        }

        composable(route = ScreenNavigation.WarriorListScreen.route,
            arguments = listOf(navArgument("type") { type = NavType.StringType})
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: ""
            val warriorType = WarriorType.fromString(type)
            WarriorListScreen(warriorType)
        }

        composable(ScreenNavigation.AddScreen.route) {
            AddScreen()
        }
    }
}