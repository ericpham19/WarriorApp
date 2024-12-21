package com.newsperform.warriorgame.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.newsperform.warriorgame.domain.model.WarriorType

@Composable
fun WarriorListScreen(
    type: WarriorType
) {
    Column {
        when(type) {
            WarriorType.KNIGHT -> Text("Knight")
            WarriorType.HUNTER -> Text("hunter")
            WarriorType.WIZARD -> Text("wizard")

        }

        Text("Hello")
    }

}