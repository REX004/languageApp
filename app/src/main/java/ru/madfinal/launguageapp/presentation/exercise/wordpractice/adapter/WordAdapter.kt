package ru.madfinal.launguageapp.presentation.exercise.wordpractice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.madfinal.launguageapp.databinding.ItemTestGameBinding

class WordAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<String, WordAdapter.ViewHolder>(StringComparator()) {

    inner class ViewHolder(private val binding: ItemTestGameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(optionText: String) {
            binding.word.text = optionText

            binding.root.setOnClickListener {
                onItemClick(optionText)
            }
        }
    }

    class StringComparator : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTestGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}