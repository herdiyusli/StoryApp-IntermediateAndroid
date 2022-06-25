package com.herdi.yusli.herdistoryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.herdi.yusli.herdistoryapp.data.RetrofitConfig
import com.herdi.yusli.herdistoryapp.data.StoryRepository
import com.herdi.yusli.herdistoryapp.database.StoryDatabase
import com.herdi.yusli.herdistoryapp.preference.AuthPreference

object Injection {
    fun provideRepository(context: Context, dataStore: DataStore<Preferences>): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = RetrofitConfig.getApiService()
        val pref = AuthPreference.getInstance(dataStore)
        return StoryRepository(database, apiService, pref)
    }
}