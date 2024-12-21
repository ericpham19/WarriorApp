package com.newsperform.warriorgame.presentation.navigation

import com.newsperform.warriorgame.domain.model.WarriorType

sealed class ScreenNavigation(val route: String) {
    object HomeScreen : ScreenNavigation("home")
    object WarriorListScreen : ScreenNavigation("list/{type}") {
        fun createRoute(type: WarriorType) = "list/$type"
    }
    object AddScreen : ScreenNavigation("add")
    object EditScreen : ScreenNavigation("edit/{type}/{id}") {
        fun createRoute(type: String, id: String) = "edit/$type/$id"
    }
}