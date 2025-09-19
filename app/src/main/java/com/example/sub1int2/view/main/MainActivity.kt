package com.example.sub1int2.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sub1int2.R
import com.example.sub1int2.data.response.ListStoryItem
import com.example.sub1int2.databinding.ActivityMainBinding
import com.example.sub1int2.view.ViewModelFactory
import com.example.sub1int2.view.addstory.AddStoryActivity
import com.example.sub1int2.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        myToolbar()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        observeViewModel()
        viewModel.fetchStory()
    }

    private fun myToolbar() {
        val myToolbar: Toolbar? = binding.topAppBar
        setSupportActionBar(myToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_story -> {
                val intent = Intent(this, AddStoryActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                viewModel.logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeViewModel() {
        viewModel.stories.observe(this, Observer { listStory ->
            setEventData(listStory)
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                android.widget.Toast.makeText(this, errorMessage, android.widget.Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun setEventData(listStory: List<ListStoryItem>) {
        val adapter = MainAdapter()
        binding.recyclerView.adapter = adapter
        adapter.submitList(listStory)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}