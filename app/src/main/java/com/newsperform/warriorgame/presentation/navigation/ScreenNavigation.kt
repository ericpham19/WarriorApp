package com.newsperform.warriorgame.presentation.navigation

import com.newsperform.warriorgame.domain.model.WarriorType

sealed class ScreenNavigation(val route: String) {
    data object HomeScreen : ScreenNavigation("home")
    data object WarriorListScreen : ScreenNavigation("list/{type}") {
        fun createRoute(type: WarriorType) = "list/$type"
    }
    data object AddEditScreen : ScreenNavigation("warrior/{type}?warriorId={warriorId}") {
        fun createRoute(type: String, warriorId: String? = null): String {
            return if (warriorId != null) {
                "warrior/$type?warriorId=$warriorId"
            } else {
                "warrior/$type"
            }
        }
    }
}