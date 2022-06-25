package com.herdi.yusli.herdistoryapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.herdi.yusli.herdistoryapp.databinding.ActivityRegisterBinding
import com.herdi.yusli.herdistoryapp.model.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        playAnimation()

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]


        viewModel.isLoading.observe(this) {
            showLoading(it)
        }


        binding.apply {
            registerButton.isEnabled = false
            val editTexts = listOf(Nama, email, password)
            for (editText in editTexts) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val nama = Nama.text.toString().trim()
                        val email = email.text.toString().trim()
                        val password = password.text.toString().trim()

                        registerButton.isEnabled = nama.isNotEmpty()
                                && isValidEmail(email)
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





        binding.registerButton.setOnClickListener { view ->
            val name = binding.Nama.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            viewModel.requestRegister(name, email, password)
            viewModel.getHasilReg().observe(this) {
                if (it == "success") {
                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                if (it == "fail") {
                    Toast.makeText(this, "Register Gagal Email Telah Digunakan", Toast.LENGTH_LONG)
                        .show()
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

    private fun playAnimation() {
        val page = ObjectAnimator.ofFloat(binding.Registertxt, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA, 1f).setDuration(500)
        val nameEt = ObjectAnimator.ofFloat(binding.Nama, View.ALPHA, 1f).setDuration(500)
        val emailEt = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(500)
        val passwordEt = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(500)
        val buttonR =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)

        val hintTxt = AnimatorSet().apply {
            playTogether(name, email,password)
        }

        val editTxt = AnimatorSet().apply {
            playTogether(nameEt, emailEt,passwordEt)
        }

        AnimatorSet().apply {
            playSequentially(page, hintTxt, editTxt, buttonR)
            startDelay = 500
        }.start()
    }

}