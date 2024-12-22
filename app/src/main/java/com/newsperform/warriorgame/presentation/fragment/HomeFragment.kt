package com.newsperform.warriorgame.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.newsperform.warriorgame.databinding.FragmentHomeBinding
import com.newsperform.warriorgame.domain.model.WarriorType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnKnight.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToWarriorList(
                        type = WarriorType.KNIGHT.name.lowercase()
                    )
                )
            }

            btnHunter.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToWarriorList(
                        type = WarriorType.HUNTER.name.lowercase()
                    )
                )
            }

            btnWizard.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToWarriorList(
                        type = WarriorType.WIZARD.name.lowercase()
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}