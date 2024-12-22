package com.newsperform.warriorgame.presentation.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.newsperform.warriorgame.databinding.DialogAddEditWarriorBinding
import com.newsperform.warriorgame.domain.model.Warrior
import com.newsperform.warriorgame.domain.model.WarriorType
import com.newsperform.warriorgame.presentation.screens.WarriorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditWarriorDialogFragment : DialogFragment() {
    private var _binding: DialogAddEditWarriorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WarriorViewModel by viewModels()
    private val args: AddEditWarriorDialogFragmentArgs by navArgs()
    private val isEditMode get() = !args.warriorId.isNullOrEmpty()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddEditWarriorBinding.inflate(layoutInflater)

        if (isEditMode) {
            viewModel.getCurrentlySelectedWarrior(args.warriorId ?: "")
        }

        binding.apply {
            layoutMp.isVisible = shouldShowMpField()
            layoutExtraStat.hint = getExtraStatLabel()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when {
                        state.isLoading -> {
                            // implement
                        }

                        state.error != null -> {
                            showError(state.error)
                        }

                        state.selectedWarrior != null && isEditMode -> {
                            addDataToFields(state.selectedWarrior)
                        }
                    }
                }
            }
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (isEditMode) "Edit Warrior" else "Add Warrior")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ -> handleSave() }
            .setNegativeButton("Cancel") { _, _ -> dismiss() }
            .create()
    }

    private fun addDataToFields(warrior: Warrior) {
        binding.apply {
            editName.setText(warrior.name)
            editHp.setText(warrior.hp.toString())
            val (extraStat, hint) = when (warrior) {
                is Warrior.Knight -> warrior.strength to "Strength"
                is Warrior.Hunter -> warrior.dexterity to "Dexterity"
                is Warrior.Wizard -> warrior.intelligence to "Intelligence"
            }

            layoutExtraStat.hint = hint
            editExtraStat.setText(extraStat.toString())

            if (warrior is Warrior.Wizard) {
                editMp.setText(warrior.mp.toString())
            }
        }
    }

    private fun handleSave() {
        binding.apply {
            val name = editName.text.toString()
            val hp = editHp.text.toString()
            val extraStat = editExtraStat.text.toString()
            val mp = if (args.type == WarriorType.WIZARD.name.lowercase()) {
                editMp.text.toString().toIntOrNull()
            } else null

            if (validateInput(name, hp, extraStat, mp)) {
                if (isEditMode) {
                    viewModel.updateWarrior(
                        name = name,
                        hp = hp,
                        extraStat = extraStat
                    )
                } else {
                    viewModel.addWarrior(
                        type = WarriorType.fromString(args.type),
                        name = name,
                        hp = hp.toIntOrNull() ?: 0,
                        mp = mp,
                        extraStat = extraStat.toIntOrNull() ?: 0
                    )
                }
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        findNavController().previousBackStackEntry?.savedStateHandle?.set("dialogDismissed", true)
    }

    private fun validateInput(name: String, hp: String, extraStat: String, mp: Int?): Boolean {
        if (name.isBlank()) {
            showError("Name cannot be empty")
            return false
        }

        if (hp.toIntOrNull() == null || hp.toInt() <= 0) {
            showError("Invalid HP value")
            return false
        }

        if (extraStat.toIntOrNull() == null || extraStat.toInt() < 0) {
            showError("Invalid stat value")
            return false
        }

        if (args.type == WarriorType.WIZARD.name.lowercase() && (mp == null || mp < 0)) {
            showError("Invalid MP value")
            return false
        }

        return true
    }


    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun getExtraStatLabel(): String {
        return when (args.type) {
            WarriorType.KNIGHT.name.lowercase() -> "Strength"
            WarriorType.HUNTER.name.lowercase() -> "Dexterity"
            WarriorType.WIZARD.name.lowercase() -> "Intelligence"
            else -> "Extra Stat"
        }
    }

    private fun shouldShowMpField(): Boolean {
        return args.type == WarriorType.WIZARD.name.lowercase()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearSelectedWarrior()
        _binding = null
    }
}