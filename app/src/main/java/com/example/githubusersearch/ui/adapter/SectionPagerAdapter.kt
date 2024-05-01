package com.example.githubusersearch.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubusersearch.ui.view.FollowerFollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String? = null

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowerFollowingFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowerFollowingFragment.ARG_POSITION, position + 1)
            putString(FollowerFollowingFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int = 2
}