package com.herdi.yusli.herdistoryapp.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.herdi.yusli.herdistoryapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading(true)

        val name = intent.getStringExtra(EXTRA_NAME)
        val deskripsi = intent.getStringExtra(EXTRA_DESKRIPSI)
        val image_url = intent.getStringExtra(EXTRA_IMAGE_URL)

        binding.apply {
            nameTv.text = name
            if (name == "Guest") {
                desTv.text = "deskripsi foto"
            } else {
                desTv.text = deskripsi
            }

            Glide.with(this@DetailStoryActivity)
                .load(image_url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .fitCenter()
                .into(ivFotoStory2)
            loading(false)
        }


        supportActionBar?.title = "Detail Story"

    }

    private fun loading(state: Boolean) {
        if (state) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                ivFotoStory.visibility = View.INVISIBLE
                nameTv.visibility = View.INVISIBLE
                desTv.visibility = View.INVISIBLE
            }
        } else {
            binding.apply {
                progressBar.visibility = View.GONE
                ivFotoStory.visibility = View.VISIBLE
                nameTv.visibility = View.VISIBLE
                desTv.visibility = View.VISIBLE
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESKRIPSI = "extra_deskripsi"
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }
}