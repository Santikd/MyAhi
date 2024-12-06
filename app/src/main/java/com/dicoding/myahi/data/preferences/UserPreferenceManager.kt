package com.dicoding.myahi.data.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class UserPreferenceManager private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveUserDetails(user: UserData) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = user.email
            preferences[TOKEN] = user.token
            preferences[IS_LOGGED_IN] = true
        }
        Log.d("UserPreferenceManager", "Saved user details: $user")
    }

    fun fetchUserDetails(): Flow<UserData> {
        Log.d("UserPreferenceManager", "Fetching user details from DataStore")
        return dataStore.data.map { preferences ->
            UserData(
                email = preferences[EMAIL] ?: "",
                token = preferences[TOKEN] ?: "",
                isLogin = preferences[IS_LOGGED_IN] ?: false
            )
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferenceManager? = null

        private val EMAIL = stringPreferencesKey("user_email")
        private val TOKEN = stringPreferencesKey("auth_token")
        private val IS_LOGGED_IN = booleanPreferencesKey("logged_in_status")

        fun getInstance(userDataStore: DataStore<Preferences>): UserPreferenceManager {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferenceManager(userDataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
