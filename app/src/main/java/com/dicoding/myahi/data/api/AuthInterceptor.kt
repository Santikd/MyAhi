package com.dicoding.myahi.data.api

import android.content.Context
import android.util.Log
import com.dicoding.myahi.data.preferences.UserPreferenceManager
import com.dicoding.myahi.data.preferences.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
class AuthInterceptor(private val context: Context) : Interceptor {

    private val maxRetries = 3
    private val retryDelayMillis = 500L // 500 milliseconds

    override fun intercept(chain: Interceptor.Chain): Response {
        val pref = UserPreferenceManager.getInstance(context.dataStore)

        var token: String? = null
        for (i in 0 until maxRetries) {
            token = runBlocking { pref.fetchUserDetails().first() }.token
            if (token != null) {
                break // Token found, exit retry loop
            }
            if (i < maxRetries - 1) {
                Thread.sleep(retryDelayMillis) // Wait before retrying
            }
        }

        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request() // Proceed without authorization header
        }

        return chain.proceed(request)
    }
}