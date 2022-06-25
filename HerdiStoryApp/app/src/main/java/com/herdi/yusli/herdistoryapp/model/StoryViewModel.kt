package com.herdi.yusli.herdistoryapp.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.herdi.yusli.herdistoryapp.data.ListStoryItem
import com.herdi.yusli.herdistoryapp.data.StoryRepository
import com.herdi.yusli.herdistoryapp.di.Injection

class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {
    val storyList: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}

class ViewModelFactory(
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository(context, dataStore)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
