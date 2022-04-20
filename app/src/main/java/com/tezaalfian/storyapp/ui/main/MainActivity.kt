package com.tezaalfian.storyapp.ui.main

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tezaalfian.storyapp.R
import com.tezaalfian.storyapp.adapter.ListStoryAdapter
import com.tezaalfian.storyapp.databinding.ActivityMainBinding
import com.tezaalfian.storyapp.ui.StoryViewModelFactory
import com.tezaalfian.storyapp.ui.login.LoginActivity
import com.tezaalfian.storyapp.data.Result
import com.tezaalfian.storyapp.data.response.ListStoryItem
import com.tezaalfian.storyapp.ui.detail.DetailActivity
import com.tezaalfian.storyapp.ui.map.MapsActivity
import com.tezaalfian.storyapp.ui.story.StoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStories.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStories.layoutManager = LinearLayoutManager(this)
        }

        title = "Story"
        setupViewModel()
    }

    private fun setupViewModel() {
        val factory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(
            this,
            factory
        )[MainViewModel::class.java]

        mainViewModel.isLogin().observe(this){
            if (!it){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        mainViewModel.getToken().observe(this){ token ->
            if (token.isNotEmpty()){
                mainViewModel.getStories(token).observe(this){result ->
                    if (result != null){
                        when(result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val stories = result.data.listStory
                                val listStoryAdapter = ListStoryAdapter(stories as ArrayList<ListStoryItem>)
                                binding.rvStories.adapter = listStoryAdapter

                                listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
                                    override fun onItemClicked(data: ListStoryItem) {
                                        showSelectedStory(data)
                                    }
                                })
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "Failure : " + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showSelectedStory(story: ListStoryItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_STORY, story)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.item_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                mainViewModel.logout()
                true
            }
            R.id.add_story -> {
                startActivity(Intent(this, StoryActivity::class.java))
                true
            }
            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.map_menu -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> true
        }
    }
}