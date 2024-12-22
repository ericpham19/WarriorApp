package com.newsperform.warriorgame.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsperform.warriorgame.domain.model.Warrior
import com.newsperform.warriorgame.domain.model.WarriorAttributes
import com.newsperform.warriorgame.domain.model.WarriorType
import com.newsperform.warriorgame.domain.usecase.AddWarriorUseCase
import com.newsperform.warriorgame.domain.usecase.DeleteWarriorUseCase
import com.newsperform.warriorgame.domain.usecase.EditWarriorUseCase
import com.newsperform.warriorgame.domain.usecase.GetWarriorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WarriorViewModel @Inject constructor(
    private val addWarriorUseCase: AddWarriorUseCase,
    private val getWarriorsUseCase: GetWarriorsUseCase,
    private val editWarriorUseCase: EditWarriorUseCase,
    private val deleteWarriorUseCase: DeleteWarriorUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WarriorUiState())
    val uiState: StateFlow<WarriorUiState> = _uiState.asStateFlow()

    private val _selectedType = MutableStateFlow<WarriorType?>(null)

    val filteredWarriors = combine(
        _uiState,
        _selectedType
    ) { state, selectedType ->
        when {
            selectedType == null -> state
            else -> state.copy(
                warriors = state.warriors.filter { it.type == selectedType }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WarriorUiState()
    )

    init {
        loadWarriors()
    }

    fun addWarrior(
        type: WarriorType,
        name: String,
        hp: Int,
        mp: Int? = null,
        extraStat: Int
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val params = AddWarriorUseCase.Params(
                type = type,
                name = name,
                stats = WarriorAttributes(hp = hp, mp = mp, extraStat = extraStat)
            )

            addWarriorUseCase(params)
                .onSuccess {
                    loadWarriors()
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
    }

    fun loadWarriors() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getWarriorsUseCase()
                .onSuccess { warriors ->
                    _uiState.update {
                        it.copy(
                            warriors = warriors,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
    }

    fun setWarriorType(type: WarriorType) {
        _selectedType.value = type
    }

    fun getCurrentlySelectedWarrior(warriorId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val warrior = _uiState.value.warriors.find { it.id == warriorId }
                ?: getWarriorsUseCase().getOrNull()?.find { it.id == warriorId }

            if (warrior != null) {
                _uiState.update {
                    it.copy(
                        selectedWarrior = warrior,
                        isLoading = false,
                        error = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Warrior not found"
                    )
                }
            }
        }
    }

    fun updateWarrior(
        name: String,
        hp: String,
        extraStat: String,
    ) {
        viewModelScope.launch {
            val currentWarrior = _uiState.value.selectedWarrior ?: return@launch

            val updatedWarrior = when (currentWarrior) {
                is Warrior.Knight -> currentWarrior.copy(
                    name = name,
                    hp = hp.toIntOrNull() ?: 0,
                    strength = extraStat.toIntOrNull() ?: 0
                )

                is Warrior.Hunter -> currentWarrior.copy(
                    name = name,
                    hp = hp.toIntOrNull() ?: 0,
                    dexterity = extraStat.toIntOrNull() ?: 0
                )

                is Warrior.Wizard -> currentWarrior.copy(
                    name = name,
                    hp = hp.toIntOrNull() ?: 0,
                    intelligence = extraStat.toIntOrNull() ?: 0,
                    mp = currentWarrior.mp
                )
            }

            editWarrior(updatedWarrior)
        }
    }

    private fun editWarrior(updatedWarrior: Warrior) {
        viewModelScope.launch {
            val params = when (updatedWarrior) {
                is Warrior.Knight -> EditWarriorUseCase.Params(
                    id = updatedWarrior.id,
                    type = WarriorType.KNIGHT,
                    name = updatedWarrior.name,
                    hp = updatedWarrior.hp,
                    extraStat = updatedWarrior.strength
                )

                is Warrior.Hunter -> EditWarriorUseCase.Params(
                    id = updatedWarrior.id,
                    type = WarriorType.HUNTER,
                    name = updatedWarrior.name,
                    hp = updatedWarrior.hp,
                    extraStat = updatedWarrior.dexterity
                )

                is Warrior.Wizard -> EditWarriorUseCase.Params(
                    id = updatedWarrior.id,
                    type = WarriorType.WIZARD,
                    name = updatedWarrior.name,
                    hp = updatedWarrior.hp,
                    extraStat = updatedWarrior.intelligence,
                    mp = updatedWarrior.mp
                )
            }

            editWarriorUseCase(params)
                .onSuccess {
                    loadWarriors()
                    clearSelectedWarrior()
                }
                .onFailure { _ ->
                    // Handle error later
                }
        }
    }

    fun deleteWarrior(warriorId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val params = DeleteWarriorUseCase.Params(id = warriorId)
            deleteWarriorUseCase(params)
                .onSuccess {
                    loadWarriors()
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
    }

    fun clearSelectedWarrior() {
        _uiState.update { it.copy(selectedWarrior = null) }
    }

    fun refreshWarriors() {
        val currentType = _selectedType.value
        _selectedType.value = null
        _selectedType.value = currentType
    }
}

data class WarriorUiState(
    val warriors: List<Warrior> = emptyList(),
    val isLoading: Boolean = false,
    val selectedWarrior: Warrior? = null,
    val error: String? = null
)
