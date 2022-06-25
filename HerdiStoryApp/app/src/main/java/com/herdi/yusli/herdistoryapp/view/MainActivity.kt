package com.herdi.yusli.herdistoryapp.view

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.herdi.yusli.herdistoryapp.R
import com.herdi.yusli.herdistoryapp.adapter.LoadingAdapter
import com.herdi.yusli.herdistoryapp.adapter.StoryAdapter
import com.herdi.yusli.herdistoryapp.data.ListStoryItem
import com.herdi.yusli.herdistoryapp.data.User
import com.herdi.yusli.herdistoryapp.databinding.ActivityMainBinding
import com.herdi.yusli.herdistoryapp.model.MainViewModel
import com.herdi.yusli.herdistoryapp.model.StoryViewModel
import com.herdi.yusli.herdistoryapp.model.ViewModelFactory
import com.herdi.yusli.herdistoryapp.preference.AuthPreference
import com.herdi.yusli.herdistoryapp.preference.AuthViewModelFactory


class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter
    private lateinit var pref: AuthPreference
    private lateinit var mainViewModel: MainViewModel
    private val storyViewModel: StoryViewModel by viewModels {
        ViewModelFactory(this, dataStore)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Story App"


        adapter = StoryAdapter()
        pref = AuthPreference.getInstance(dataStore)

        mainViewModel =
            ViewModelProvider(this, AuthViewModelFactory(pref))[MainViewModel::class.java]


        mainViewModel.getToken().observe(this)
        { user: User ->
            if (user.token != "") {
                binding.namauserTv.text = user.name
            }
        }

        binding.apply {
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                listStoryRv.layoutManager = GridLayoutManager(this@MainActivity, 2)
            } else {
                listStoryRv.layoutManager = LinearLayoutManager(this@MainActivity)
            }
            listStoryRv.adapter = adapter
            listStoryRv.setHasFixedSize(true)
        }

        getData()

        binding.buttonLogout.setOnClickListener {
            mainViewModel.saveToken(User("", "", ""))
            Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        binding.addStoryButton.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.listStoryRv.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter {
                adapter.retry()
            }
        )
        adapter.setOnItemClicked(object : StoryAdapter.OnItemClick {
            override fun onItemClicked(data: ListStoryItem) {


                Intent(this@MainActivity, DetailStoryActivity::class.java).also {
                    it.putExtra(DetailStoryActivity.EXTRA_NAME, data.name)
                    it.putExtra(DetailStoryActivity.EXTRA_DESKRIPSI, data.description)
                    it.putExtra(DetailStoryActivity.EXTRA_IMAGE_URL, data.photoUrl)
                    startActivity(it)
                    finish()
                }
            }

        })
        storyViewModel.storyList.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.maps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maps -> {
                Intent(this, MapsStoryActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}