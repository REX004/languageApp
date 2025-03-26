package ru.madfinal.launguageapp.presentation.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.madfinal.launguageapp.databinding.ItemTopUserBinding
import ru.madfinal.launguageapp.domain.models.User

class TopUsersAdapter(
    private val context: Context
) : ListAdapter<User, TopUsersAdapter.ViewHolder>(Comparator()) {
    inner class ViewHolder(private val binding: ItemTopUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            binding.userNick.text = "${user.lastName} ${user.name}"
            binding.userPoints.text = "${user.score} points"
            Glide.with(context)
                .load(user.image)
                .into(binding.userImage)
        }
    }

    class Comparator() : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTopUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}