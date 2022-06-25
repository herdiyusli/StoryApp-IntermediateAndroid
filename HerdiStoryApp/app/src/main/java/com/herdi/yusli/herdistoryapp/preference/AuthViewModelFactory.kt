package com.herdi.yusli.herdistoryapp.preference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herdi.yusli.herdistoryapp.model.MainViewModel

class AuthViewModelFactory(private val pref: AuthPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}