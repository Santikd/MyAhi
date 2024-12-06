package com.dicoding.myahi.view.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myahi.R
import com.dicoding.myahi.data.response.ListStoryItem
import com.dicoding.myahi.databinding.ActivityMainBinding
import com.dicoding.myahi.view.ViewModelFactory
import com.dicoding.myahi.view.adapter.StoryListAdapter
import com.dicoding.myahi.view.add.AddStoryActivity
import com.dicoding.myahi.view.landing.LandingActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, LandingActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                setupAction()
            }
        }
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout) {
            viewModel.logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupAction() {

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        viewModel.messages.observe(this){
            showAlert(it)
        }

        lifecycleScope.launch {
            viewModel.getStories()
        }
        viewModel.listStories.observe(this) {
            setStoriesData(it)
        }
    }

    private fun setStoriesData(stories: List<ListStoryItem?>) {
        val adapter = StoryListAdapter()
        adapter.submitList(stories)
        binding.rvStories.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showAlert(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
