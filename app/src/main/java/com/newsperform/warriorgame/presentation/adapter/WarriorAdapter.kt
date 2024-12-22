package com.newsperform.warriorgame.presentation.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.PopupWindow
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.newsperform.warriorgame.databinding.AttackBubbleBinding
import com.newsperform.warriorgame.databinding.WarriorCardBinding
import com.newsperform.warriorgame.domain.model.Warrior

class WarriorAdapter(
    private val onEditClick: (Warrior) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<Warrior, WarriorAdapter.WarriorViewHolder>(WarriorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarriorViewHolder {
        val binding = WarriorCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WarriorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WarriorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WarriorViewHolder(
        private val binding: WarriorCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { toggleCard() }
            setupClickListener(binding.btnEdit) { onEditClick(it) }
            setupClickListener(binding.btnDelete) { onDeleteClick(it.id) }
            setupClickListener(binding.btnAttack) {
                showSpeechBubble(it)
            }
        }

        fun bind(warrior: Warrior) {
            binding.apply {
                name.text = warrior.name
                type.text = warrior.type.name
                hp.text = "HP: ${warrior.hp}"

                val extraStatText = when (warrior) {
                    is Warrior.Knight -> "Strength: ${warrior.strength}"
                    is Warrior.Hunter -> "Dexterity: ${warrior.dexterity}"
                    is Warrior.Wizard -> "Intelligence: ${warrior.intelligence}\nMP: ${warrior.mp}"
                }
                extraStat.text = extraStatText
            }
        }

        private fun toggleCard() {
            val expandedCard = binding.expandedCard
            if (expandedCard.visibility == View.VISIBLE) {
                expandedCard.visibility = View.GONE
            } else {
                expandedCard.visibility = View.VISIBLE
            }
        }

        private fun setupClickListener(view: View, action: (Warrior) -> Unit) {
            view.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    action(getItem(position))
                }
            }
        }

        private fun showSpeechBubble(warrior: Warrior) {
            val context = binding.root.context
            val bubbleBinding = AttackBubbleBinding.inflate(LayoutInflater.from(context))

            val popupWindow = PopupWindow(
                bubbleBinding.root,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            bubbleBinding.bubbleText.text = when (warrior) {
                is Warrior.Knight -> "Swoosh!"
                is Warrior.Hunter -> "Pow!"
                is Warrior.Wizard -> "Zzzzt!"
            }

            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)

            val scaleAnimation = ScaleAnimation(
                0.5f, 1f,
                0.5f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 2000
                fillAfter = true
            }
            bubbleBinding.root.startAnimation(scaleAnimation)
            bubbleBinding.root.postDelayed(
                { popupWindow.dismiss() },
                2000
            )
        }
    }

    private class WarriorDiffCallback : DiffUtil.ItemCallback<Warrior>() {
        override fun areItemsTheSame(oldItem: Warrior, newItem: Warrior): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Warrior, newItem: Warrior): Boolean {
            return oldItem == newItem
        }
    }
}