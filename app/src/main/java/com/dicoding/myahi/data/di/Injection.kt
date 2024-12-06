package com.dicoding.myahi.data.di

import android.content.Context
import com.dicoding.myahi.data.repository.UserRepository
import com.dicoding.myahi.data.api.ApiConfig
import com.dicoding.myahi.data.preferences.UserPreferenceManager
import com.dicoding.myahi.data.preferences.dataStore
object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig().getApiService(context)
        val pref = UserPreferenceManager.getInstance(context.dataStore)
        return UserRepository.getInstance(pref, apiService)
    }
}