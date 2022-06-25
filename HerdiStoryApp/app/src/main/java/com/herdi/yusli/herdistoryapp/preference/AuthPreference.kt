package com.herdi.yusli.herdistoryapp.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.herdi.yusli.herdistoryapp.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreference(private val dataStore: DataStore<Preferences>) {


    private val API_KEY = stringPreferencesKey("token_user")

    private val NAME_KEY = stringPreferencesKey("name_user")

    private val ID_KEY = stringPreferencesKey("id_user")




    fun getToken(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[ID_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[API_KEY] ?: ""
            )

        }

    }


    suspend fun saveToken(user: User) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.userId
            preferences[NAME_KEY] = user.name
            preferences[API_KEY] = user.token
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): AuthPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}