package com.newsperform.warriorgame.presentation

import AddEditWarriorDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.newsperform.warriorgame.domain.model.WarriorType
import com.newsperform.warriorgame.presentation.navigation.ScreenNavigation
import com.newsperform.warriorgame.presentation.screens.HomeScreen
import com.newsperform.warriorgame.presentation.screens.WarriorListScreen
import com.newsperform.warriorgame.presentation.screens.WarriorViewModel

@Composable
fun WarriorGameApp(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val viewModel: WarriorViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = ScreenNavigation.HomeScreen.route,
        modifier = modifier
    ) {
        composable(ScreenNavigation.HomeScreen.route) {
            HomeScreen(navController) { }
        }

        composable(
            route = ScreenNavigation.WarriorListScreen.route,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: ""
            val warriorType = WarriorType.fromString(type)
            WarriorListScreen(warriorType, viewModel = viewModel,
                onAddButtonClicked = {
                    navController.navigate(route = ScreenNavigation.AddEditScreen.createRoute(type = type))
                },
                onEditClick = { warriorId ->
                    navController.navigate(
                        route = ScreenNavigation.AddEditScreen.createRoute(
                            type,
                            warriorId
                        )
                    )
                })
        }

        dialog(
            route = ScreenNavigation.AddEditScreen.route,
            arguments = listOf(
                navArgument("warriorId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("type") {
                    type = NavType.StringType
                },
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("warriorId") ?: ""
            val type = backStackEntry.arguments?.getString("type") ?: ""
            val warriorType = WarriorType.fromString(type)

            AddEditWarriorDialog(
                warriorId = id,
                warriorType = warriorType,
                warriorViewModel = viewModel,
                onDismiss = { navController.popBackStack() },
            )
        }
    }
}