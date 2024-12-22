package com.newsperform.warriorgame.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.newsperform.warriorgame.databinding.FragmentWarriorListBinding
import com.newsperform.warriorgame.domain.model.WarriorType
import com.newsperform.warriorgame.presentation.adapter.WarriorAdapter
import com.newsperform.warriorgame.presentation.screens.WarriorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WarriorListFragment : Fragment() {
    private var _binding: FragmentWarriorListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WarriorViewModel by viewModels()
    private val args: WarriorListFragmentArgs by navArgs()

    private val warriorAdapter = WarriorAdapter(
        onEditClick = { warrior ->
            findNavController().navigate(
                WarriorListFragmentDirections.actionListToAddEdit(
                    type = warrior.type.name.lowercase(),
                    warriorId = warrior.id
                )
            )
        },
        onDeleteClick = { id -> viewModel.deleteWarrior(id) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWarriorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            adapter = warriorAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val warriorType = WarriorType.fromString(args.type)
        viewModel.setWarriorType(warriorType)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredWarriors.collect { state ->
                    when {
                        state.isLoading -> showLoading()
                        state.error != null -> showError(state.error)
                        else -> {
                            hideLoading()
                            warriorAdapter.submitList(state.warriors)
                            if (state.warriors.isEmpty()) {
                                binding.noDataText.visibility = View.VISIBLE
                            } else {
                                binding.noDataText.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }

        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.let { savedState ->
            savedState.getLiveData<Boolean>("dialogDismissed")
                .observe(viewLifecycleOwner) { dismissed ->
                    if (dismissed) {
                        viewModel.loadWarriors()
                    }
                }
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(
                WarriorListFragmentDirections.actionListToAddEdit(
                    type = args.type,
                    warriorId = null
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.filteredWarriors.value.warriors.isNotEmpty()) {
            binding.noDataText.visibility = View.GONE
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
    }

    private fun hideLoading() {
        binding.progressBar.isVisible = false
    }

    private fun showError(message: String) {
        binding.progressBar.isVisible = false
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}