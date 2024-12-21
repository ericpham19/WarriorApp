package com.newsperform.warriorgame.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.newsperform.warriorgame.domain.model.WarriorType
import com.newsperform.warriorgame.presentation.navigation.ScreenNavigation

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    onWarriorButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            WarriorTypeButton(
                type = WarriorType.KNIGHT,
                text = "Knight",
                navHostController = navHostController
            )
            WarriorTypeButton(
                type = WarriorType.HUNTER,
                text = "Hunter",
                navHostController = navHostController
            )
            WarriorTypeButton(
                type = WarriorType.WIZARD,
                text = "Wizard",
                navHostController = navHostController
            )
        }
    }
}

@Composable
private fun WarriorTypeButton(
    type: WarriorType,
    text: String,
    navHostController: NavHostController
) {
    Button(onClick = {
        navHostController.navigate(ScreenNavigation.WarriorListScreen.createRoute(type))
    }) {
        Text(text)
    }
}