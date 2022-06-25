package com.herdi.yusli.herdistoryapp.model

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.herdi.yusli.herdistoryapp.data.LoginResponse
import com.herdi.yusli.herdistoryapp.data.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<LoginResponse>()

    private val _hasil = MutableLiveData<String?>()
    val hasil: MutableLiveData<String?> = _hasil

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun requestLogin(email: String, password: String) {
        _isLoading.value = true
        _hasil.value = null
        val client = RetrofitConfig.getApiService().loginRequest(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    user.postValue(response.body())
                    hasil.value = "success"
                    Log.e(TAG, "Login Success")
                } else {
                    hasil.value = "fail"
                    Log.e(TAG, "Login Failure")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }
        })
    }


    fun getUserProfile(): MutableLiveData<LoginResponse> {
        return user
    }

    fun getHasilLog(): MutableLiveData<String?> {
        return hasil
    }


}