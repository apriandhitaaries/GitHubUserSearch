package com.example.githubusersearch.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersearch.ui.adapter.ListUserAdapter
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.databinding.FragmentFollowerFollowingBinding
import com.example.githubusersearch.ui.viewmodel.FollowerFollowingViewModel

class FollowerFollowingFragment : Fragment() {
    private var _binding: FragmentFollowerFollowingBinding? = null
    private val binding get() = _binding
    private val followerFollowingViewModel by viewModels<FollowerFollowingViewModel>()

    private var position = 0
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowerFollowingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1) {
            // Follower
            if (followerFollowingViewModel.listFollower.value == null) {
                followerFollowingViewModel.getFollowers(username.orEmpty())
            } else {
                followerFollowingViewModel.setUsername(username)
            }

            followerFollowingViewModel.listFollower.observe(viewLifecycleOwner) { followers ->
                if (followers.isNullOrEmpty()) {
                    binding?.tvNoFollowers?.visibility = View.VISIBLE
                    binding?.rvFollow?.visibility = View.GONE
                    binding?.tvNoFollowers?.text = "This user does not have any followers"
                } else {
                    binding?.tvNoFollowers?.visibility = View.GONE
                    binding?.rvFollow?.visibility = View.VISIBLE
                    showRecyclerView(followers)
                }
            }

            followerFollowingViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }

        } else  {
            Log.d("username", username.toString())
            // Following
            if (followerFollowingViewModel.listFollowing.value == null) {
                followerFollowingViewModel.getFollowing(username.orEmpty())
            } else {
                followerFollowingViewModel.setUsername(username)
            }

            followerFollowingViewModel.listFollowing.observe(viewLifecycleOwner) { following ->
                if (following.isNullOrEmpty()) {
                    binding?.tvNoFollowing?.visibility = View.VISIBLE
                    binding?.rvFollow?.visibility = View.GONE
                    binding?.tvNoFollowers?.text = "This user does not follow anyone"
                } else {
                    binding?.tvNoFollowers?.visibility = View.GONE
                    binding?.rvFollow?.visibility = View.VISIBLE
                    showRecyclerView(following)
                }
            }

            followerFollowingViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }
    }

    private fun showRecyclerView(users: List<ItemsItem>?) {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvFollow?.layoutManager = layoutManager
        val adapter = ListUserAdapter()
        binding?.rvFollow?.adapter = adapter
        adapter.submitList(users)

        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding?.rvFollow?.addItemDecoration(itemDecoration)
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}