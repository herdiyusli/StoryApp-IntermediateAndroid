package com.herdi.yusli.herdistoryapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.herdi.yusli.herdistoryapp.data.User
import com.herdi.yusli.herdistoryapp.preference.AuthPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: AuthPreference) : ViewModel() {
    fun getToken(): LiveData<User> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(user: User) {
        viewModelScope.launch {
            pref.saveToken(user)
        }
    }

}
