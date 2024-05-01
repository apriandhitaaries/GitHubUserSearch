package com.example.githubusersearch.ui.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersearch.ui.adapter.ListUserAdapter
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.databinding.ActivityFavoriteUserBinding
import com.example.githubusersearch.ui.viewmodel.FavoriteViewModel
import com.example.githubusersearch.ui.viewmodel.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        favoriteViewModel.getAllFavorite().observe(this) { users ->
            if (users.isEmpty()) {
                showEmptyMessage()
            } else {
                val items = ArrayList<ItemsItem>()
                users.map {
                    val item = ItemsItem(
                        login = it.username,
                        avatarUrl = it.avatarUrl
                    )
                    items.add(item)
                }
                showRecyclerView(items)
            }
            showLoading(false)
        }

    }

    private fun showRecyclerView(users: List<ItemsItem>) {
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        val adapter = ListUserAdapter()
        binding.rvFavorite.adapter = adapter
        adapter.submitList(users)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyMessage() {
        binding.tvNoFavorite.visibility = View.VISIBLE
        binding.rvFavorite.visibility = View.GONE
        binding.tvNoFavorite.text = "Favorite User not added yet"
    }
}