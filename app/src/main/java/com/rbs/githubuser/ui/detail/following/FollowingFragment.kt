package com.rbs.githubuser.ui.detail.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rbs.githubuser.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var username: String
    private lateinit var adapter: FollowingAdapter
    private val followingViewModel by viewModels<FollowingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        username = arguments?.getString(USERNAME).toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializationAdapter()
        followingViewModel.getFollowing(username)
        followingViewModel.getData().observe(viewLifecycleOwner) {
            if (it != null) {
                setLoadingData()
                adapter.setListUsers(it)
            }
        }
    }

    private fun initializationAdapter() {
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = LinearLayoutManager(context)
        adapter = FollowingAdapter()
        binding.rvUsers.adapter = adapter
    }

    private fun setLoadingData() {
        binding.progressBar.visibility = View.GONE
        binding.rvUsers.visibility = View.VISIBLE
    }

    companion object {
        const val USERNAME = "username"

        fun newInstance(username: String): FollowingFragment {
            return FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, username)
                }
            }
        }
    }
}