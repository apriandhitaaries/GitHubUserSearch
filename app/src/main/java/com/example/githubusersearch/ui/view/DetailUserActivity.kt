package com.example.githubusersearch.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubusersearch.R
import com.example.githubusersearch.ui.adapter.SectionPagerAdapter
import com.example.githubusersearch.data.local.database.FavoriteUser
import com.example.githubusersearch.databinding.ActivityDetailUserBinding
import com.example.githubusersearch.ui.viewmodel.FavoriteViewModel
import com.example.githubusersearch.ui.viewmodel.DetailUserViewModel
import com.example.githubusersearch.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailUserViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USER)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        if (detailViewModel.user.value == null) {
            detailViewModel.detailUser(username.orEmpty())
        } else {
            detailViewModel.setUsername(username)
        }

        detailViewModel.user.observe(this) { user ->
            Log.d("User", user.toString())
            if (user != null) {
                binding.apply {
                    Glide.with(this@DetailUserActivity)
                        .load(user.avatarUrl)
                        .into(imgDetailAvatar)

                    tvDetailUsername.text = user.login
                    tvDetailName.text = user.name
                    tvDetailBio.text = user.bio ?: "Bio not available"
                    tvDetailLocation.text = user.location ?: "Location not specified"
                    tvDetailFollowers.text = "${user.followers} Followers"
                    tvDetailFollowing.text = "${user.following} Following"
                }
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.fabFavorite.setOnClickListener {
            val userDetail = detailViewModel.user.value

            if (userDetail != null) {
                val avatarUrl = userDetail.avatarUrl
                val favoriteUser = FavoriteUser(
                    username = username.orEmpty(),
                    avatarUrl = avatarUrl
                )

                //cek apakah user masuk dalam favorite atau bukan
                if (favoriteViewModel.isFavorite.value == true) {
                    favoriteViewModel.delete(favoriteUser)
                    Toast.makeText(this, "User removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    favoriteViewModel.insert(favoriteUser)
                    Toast.makeText(this, "User added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }

        favoriteViewModel.checkFavoriteUser(username.orEmpty(), this)

        favoriteViewModel.isFavorite.observe(this) { isFavorite ->
            if (isFavorite) {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}