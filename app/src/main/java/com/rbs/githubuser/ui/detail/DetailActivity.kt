package com.rbs.githubuser.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.rbs.githubuser.R
import com.rbs.githubuser.data.DetailUser
import com.rbs.githubuser.databinding.ActivityDetailBinding
import com.rbs.githubuser.db.DetailUserDB

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        username = intent.getStringExtra(USERNAME).toString()
        setViewModel()
        setPagerAdapter()
    }

    private fun setViewModel() {
        val detailViewModel by viewModels<DetailViewModel> {
            DetailViewModel.ViewModelFactory.getInstance(application)
            DetailViewModel.ViewModelFactory(application)
        }

        detailViewModel.getDetailDataByUsername(username)
        detailViewModel.getData().observe(this) {
            setLoadingData()
            setUsersData(it, detailViewModel)
        }
    }

    private fun setLoadingData() {
        binding.progressBar.visibility = View.GONE
        binding.container.visibility = View.VISIBLE
    }

    private fun setUsersData(users: DetailUser, detailViewModel: DetailViewModel) {
        setView(binding.tvName, users.name)
        setView(binding.tvUsername, users.username)
        setView(binding.tvUrl, users.url)
        setView(binding.tvCompany, users.company)
        setImage(binding.ivAvatar, users.avatar)

        binding.btnFavorite.setOnClickListener {
            val userRoom =
                DetailUserDB(users.name, users.username, users.avatar, users.url, users.company)
            detailViewModel.insertData(userRoom)
        }
    }

    private fun setView(view: TextView, value: String?) {
        if (value != null) {
            view.text = value
        } else {
            view.visibility = View.GONE
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setImage(view: ImageView, value: String?) {
        if (value != null) {
            Glide.with(this)
                .load(value)
                .into(view)
        } else {
            view.visibility = View.GONE
        }
    }

    private fun setPagerAdapter() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager = binding.viewPager
        val tabs = binding.tabLayout

        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    companion object {
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.title_followers, R.string.title_following)
    }
}