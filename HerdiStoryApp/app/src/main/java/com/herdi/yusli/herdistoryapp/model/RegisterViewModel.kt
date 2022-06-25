package com.herdi.yusli.herdistoryapp.model

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.herdi.yusli.herdistoryapp.data.RegisterResponse
import com.herdi.yusli.herdistoryapp.data.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(application: Application) : AndroidViewModel(application)  {
    private val _hasil = MutableLiveData<String?>()
    val hasil: MutableLiveData<String?> = _hasil

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun requestRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        _hasil.value = null
        val client = RetrofitConfig.getApiService().registerRequest(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    hasil.value = "success"
                    Log.e(ContentValues.TAG, "Berhasil Register")
                } else {
                    hasil.value = "fail"
                    Log.e(ContentValues.TAG, "Failure: Register")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "Failure: ${t.message}")
            }
        })
    }

    fun getHasilReg(): MutableLiveData<String?> {
        return hasil
    }

}