package com.example.githubusersearch.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersearch.R
import com.example.githubusersearch.SettingPreferences
import com.example.githubusersearch.ui.adapter.ListUserAdapter
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.dataStore
import com.example.githubusersearch.databinding.ActivityMainBinding
import com.example.githubusersearch.ui.viewmodel.MainViewModel
import com.example.githubusersearch.ui.viewmodel.SettingViewModelFactory
import com.example.githubusersearch.ui.viewmodel.SwitchThemeViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    val username = searchView.text.toString().trim()
                    if (username.isNotEmpty()) {
                        mainViewModel.getUser(username)
                        searchBar.setText(searchView.text)
                    } else {
                        Toast.makeText(this@MainActivity, "Please enter a username", Toast.LENGTH_SHORT).show()
                    }
                    searchView.hide()
                    false
                }
        }

        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_favorite -> {
                    val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.menu_dark_mode -> {
                    val intent = Intent(this@MainActivity, SwitchThemeActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        observeThemeSettings()

        mainViewModel.listUser.observe(this) {
            showRecyclerView(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun observeThemeSettings() {
        val switchThemeViewModel by viewModels<SwitchThemeViewModel> {
            SettingViewModelFactory(SettingPreferences.getInstance(application.dataStore))
        }

        switchThemeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.searchBar.menu.findItem(R.id.menu_favorite)?.setIcon(R.drawable.ic_favorite_white)
                binding.searchBar.menu.findItem(R.id.menu_dark_mode)?.setIcon(R.drawable.ic_dark_mode_white)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun showRecyclerView(users: List<ItemsItem?>?) {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val adapter = ListUserAdapter()
        binding.rvUser.adapter = adapter
        adapter.submitList(users)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}