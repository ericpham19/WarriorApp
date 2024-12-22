import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.newsperform.warriorgame.domain.model.Warrior
import com.newsperform.warriorgame.domain.model.WarriorType
import com.newsperform.warriorgame.presentation.screens.WarriorViewModel

@Composable
fun AddEditWarriorDialog(
    warriorId: String?,
    warriorType: WarriorType,
    warriorViewModel: WarriorViewModel,
    onDismiss: () -> Unit,
) {
    val uiState by warriorViewModel.uiState.collectAsState()
    val selectedWarrior = uiState.selectedWarrior
    val isEditMode = selectedWarrior != null

    LaunchedEffect(warriorId) {
        if (!warriorId.isNullOrEmpty()) {
            warriorViewModel.getCurrentlySelectedWarrior(warriorId)
        }
    }

    val handleDismiss = {
        warriorViewModel.clearSelectedWarrior()
        onDismiss()
    }

    var name by remember(selectedWarrior) {
        mutableStateOf(selectedWarrior?.name ?: "")
    }
    var hp by remember(selectedWarrior) {
        mutableStateOf(selectedWarrior?.hp?.toString() ?: "")
    }

    var mp by remember(selectedWarrior) {
        mutableStateOf(
            when (selectedWarrior) {
                is Warrior.Wizard -> selectedWarrior.mp.toString()
                else -> ""
            }
        )
    }

    var extraStat by remember(selectedWarrior) {
        mutableStateOf(
            when (selectedWarrior) {
                is Warrior.Knight -> selectedWarrior.strength.toString()
                is Warrior.Hunter -> selectedWarrior.dexterity.toString()
                is Warrior.Wizard -> selectedWarrior.intelligence.toString()
                null -> ""
            }
        )
    }

    if (uiState.isLoading) {
        CircularProgressIndicator()
        return
    }

    AlertDialog(
        onDismissRequest = handleDismiss,
        title = {
            Text(if (isEditMode) "Edit Warrior" else "Add Warrior")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = hp,
                    onValueChange = { hp = it },
                    label = { Text("HP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                if (warriorType == WarriorType.WIZARD) {
                    OutlinedTextField(
                        value = mp,
                        onValueChange = { mp = it },
                        label = { Text("MP") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Extra stat field label changes based on warrior type
                val extraStatLabel = when (warriorType) {
                    WarriorType.KNIGHT -> "Strength"
                    WarriorType.HUNTER -> "Dexterity"
                    WarriorType.WIZARD -> "Intelligence"
                }

                OutlinedTextField(
                    value = extraStat,
                    onValueChange = { extraStat = it },
                    label = { Text(extraStatLabel) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isEditMode) {
                        warriorViewModel.updateWarrior(
                            name = name,
                            hp = hp,
                            extraStat = extraStat
                        )
                    } else {
                            warriorViewModel.addWarrior(
                                name = name,
                                hp = hp.toIntOrNull() ?: 0,
                                mp = mp.toIntOrNull() ?: 0,
                                type = warriorType,
                                extraStat = extraStat.toIntOrNull() ?: 0
                            )
                        }
                    handleDismiss()
                },
                enabled = name.isNotBlank() &&
                        hp.isNotBlank() &&
                        extraStat.isNotBlank()
            ) {
                Text(if (isEditMode) "Save" else "Add")
            }
        },
        dismissButton = {
            TextButton(onClick = handleDismiss) {
                Text("Cancel")
            }
        }
    )
}