package com.herdi.yusli.herdistoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.herdi.yusli.herdistoryapp.database.StoryDatabase
import com.herdi.yusli.herdistoryapp.preference.AuthPreference

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: RetrofitApiService,
    private val pref: AuthPreference
) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, pref)
            }
        ).liveData
    }
}