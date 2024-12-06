package com.dicoding.myahi.data.repository

import android.util.Log
import com.dicoding.myahi.data.api.ApiService
import com.dicoding.myahi.data.preferences.UserData
import com.dicoding.myahi.data.preferences.UserPreferenceManager
import com.dicoding.myahi.data.response.ErrorResponse
import com.dicoding.myahi.data.response.LoginResponse
import com.dicoding.myahi.data.response.StoryDetail
import com.dicoding.myahi.data.response.ListStoryItem
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val preferenceManager: UserPreferenceManager,
    private val apiService: ApiService

) {
    suspend fun storeSession(user: UserData) {
        Log.d("UserRepository", "Storing user session: $user")
        preferenceManager.saveUserDetails(user)
    }

    fun retrieveSession(): Flow<UserData> {
        return preferenceManager.fetchUserDetails()
    }

    suspend fun signOut() {
        preferenceManager.clearSession()
    }

    suspend fun registerUser(name: String, email: String, password: String): String{
        return apiService.register(name, email, password).responseMessage.toString()
    }

    suspend fun loginUser(email: String, password: String): LoginResponse{
        return apiService.login(email, password)
    }

    suspend fun getStories() : List<ListStoryItem> {
        return apiService.getStories().listStory
    }
    suspend fun getDetailStory(id: String) : StoryDetail? {
        val response = apiService.getDetailStory(id).story
        Log.d("API_RESPONSE",  "$response")
        return response
    }
    suspend fun addStory(file: MultipartBody.Part, desc: RequestBody): ErrorResponse{
        return apiService.addStories(file, desc)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            preferenceManager: UserPreferenceManager,
            apiClient: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(preferenceManager, apiClient)
            }.also { instance = it }
    }
}
