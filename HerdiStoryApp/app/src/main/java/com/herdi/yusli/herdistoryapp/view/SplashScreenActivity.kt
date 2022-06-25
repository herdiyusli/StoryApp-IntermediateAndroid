package com.herdi.yusli.herdistoryapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.herdi.yusli.herdistoryapp.data.User
import com.herdi.yusli.herdistoryapp.databinding.ActivitySplashScreenBinding
import com.herdi.yusli.herdistoryapp.model.MainViewModel
import com.herdi.yusli.herdistoryapp.preference.AuthPreference
import com.herdi.yusli.herdistoryapp.preference.AuthViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = AuthPreference.getInstance(dataStore)
        val mainViewModel =
            ViewModelProvider(this, AuthViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.getToken().observe(
            this
        ) { user: User ->
            if (user.token != "") {
                Intent(this@SplashScreenActivity, MainActivity::class.java).also {
                    mainViewModel.saveToken(User(user.userId, user.name, user.token))
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(it)
                        finish()
                    }, 1200)
                }
            } else {
                Intent(this@SplashScreenActivity, LoginActivity::class.java).also {
                    mainViewModel.saveToken(User("", "", ""))
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(it)
                        finish()
                    }, 1200)
                }
            }
        }
    }
}