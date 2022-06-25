package com.herdi.yusli.herdistoryapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.herdi.yusli.herdistoryapp.data.User
import com.herdi.yusli.herdistoryapp.databinding.ActivityLoginBinding
import com.herdi.yusli.herdistoryapp.model.LoginViewModel
import com.herdi.yusli.herdistoryapp.model.MainViewModel
import com.herdi.yusli.herdistoryapp.preference.AuthPreference
import com.herdi.yusli.herdistoryapp.preference.AuthViewModelFactory


class LoginActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val pref = AuthPreference.getInstance(dataStore)
        val mainViewModel =
            ViewModelProvider(this, AuthViewModelFactory(pref))[MainViewModel::class.java]


        binding.apply {
            loginButton.isEnabled = false
            val editTexts = listOf(emailTxt, passwordTxt)
            for (editText in editTexts) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val email = emailTxt.text.toString().trim()
                        val password = passwordTxt.text.toString().trim()

                        loginButton.isEnabled = isValidEmail(email)
                                && isValidPass(password)
                    }


                    override fun beforeTextChanged(
                        s: CharSequence, start: Int, count: Int, after: Int
                    ) {
                    }

                    override fun afterTextChanged(
                        s: Editable
                    ) {
                    }
                })
            }
        }


        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        binding.loginButton.setOnClickListener { view ->
            val email = binding.emailTxt.text.toString().trim()
            val password = binding.passwordTxt.text.toString().trim()
            viewModel.requestLogin(email, password)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            viewModel.getHasilLog().observe(this) {
                if (it == "success") {
                    Toast.makeText(this, "Login Berhasil", Toast.LENGTH_LONG).show()
                    binding.emailTxt.text = null
                    binding.passwordTxt.text = null
                    binding.passwordTxt.clearFocus()
                }
                if (it == "fail") {
                    Toast.makeText(
                        this,
                        "Login Gagal Username atau Password Salah",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            viewModel.getUserProfile().observe(this) {
                val name = it.loginResult.name
                val token = it.loginResult.token
                val id = it.loginResult.userId
                Intent(this@LoginActivity, MainActivity::class.java).also {
                    mainViewModel.saveToken(User(id, name, token))
                    startActivity(it)
                    finish()
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPass(password: CharSequence): Boolean {
        if (password.length > 5) {
            return true
        }
        return false
    }


}