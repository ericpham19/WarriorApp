package com.newsperform.warriorgame.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.newsperform.warriorgame.domain.model.WarriorType

@Composable
fun AddEditScreen(
    warriorType: WarriorType,
    warriorId: String?,
    viewModel: WarriorViewModel = hiltViewModel(),
    navController: NavHostController
) {

    LaunchedEffect(warriorId) {
        if(warriorId != null) {
            viewModel.getCurrentlySelectedWarrior(warriorId)
        }
    }
    Column {
        Text("Hello This is Add Screen")
    }
}