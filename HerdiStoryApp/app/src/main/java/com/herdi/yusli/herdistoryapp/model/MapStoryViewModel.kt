package com.herdi.yusli.herdistoryapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.herdi.yusli.herdistoryapp.data.ListStoryItem
import com.herdi.yusli.herdistoryapp.data.RetrofitConfig
import com.herdi.yusli.herdistoryapp.data.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapStoryViewModel : ViewModel() {

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory


    fun getMapStory(token: String): String {
        val client = RetrofitConfig.getApiService().getStoryMapLocation("Bearer $token", 1)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    _listStory.postValue(response.body()?.listStory)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return token
    }

    companion object {
        private const val TAG = "MapStoryViewModel"
    }
}