package com.newsperform.warriorgame.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.newsperform.warriorgame.domain.model.Warrior
import com.newsperform.warriorgame.domain.model.WarriorType
import com.newsperform.warriorgame.presentation.navigation.ScreenNavigation

@Composable
fun WarriorListScreen(
    type: WarriorType,
    viewModel: WarriorViewModel,
    onAddButtonClicked: () -> Unit,
    onEditClick: (String) -> Unit
) {
    val uiState by viewModel.filteredWarriors.collectAsStateWithLifecycle()

    LaunchedEffect(type) {
        viewModel.setWarriorType(type)
    }

    Column {
        Row(modifier = Modifier) {
            Button(
                onClick = { onAddButtonClicked() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add")
            }
        }

        if (uiState.warriors.isEmpty()) {
            Text("I am empty ")
        } else {
            uiState.warriors.forEach { warrior ->
                WarriorCard(warrior = warrior) {
                   onEditClick(warrior.id)
                }
            }
        }
    }
}


@Composable
fun WarriorCard(
    modifier: Modifier = Modifier, warrior: Warrior,
    onEditClicked: (Warrior) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedCard(
        modifier = modifier,
        onClick = {
            expanded = !expanded
        }) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(text = warrior.name)

                if (expanded) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = warrior.type.name)
                        Button(onClick = { onEditClicked(warrior) }) {
                            Text("Edit")
                        }
                    }
                }
            }
        }
    }
}