package com.example.githubusersearch.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.databinding.ItemRowUserBinding
import com.example.githubusersearch.ui.view.DetailUserActivity

class ListUserAdapter : ListAdapter<ItemsItem, ListUserAdapter.ListUserViewHolder>(DIFF_CALLBACK) {

    class ListUserViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.tvItemUsername.text = user.login
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .into(binding.imgAvatar)
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, user.login)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}