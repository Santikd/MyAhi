package com.dicoding.myahi.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.dicoding.myahi.data.repository.UserRepository
import com.dicoding.myahi.data.response.ErrorResponse
import com.dicoding.myahi.data.preferences.UserData
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess
    private val _messages = MutableLiveData<String?>()
    val messages: LiveData<String?> = _messages
    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token
    fun saveSession(user: UserData) {
        viewModelScope.launch {
            repository.storeSession(user)
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.loginUser(email, password)
                _isLoading.value = false

                val authToken = response.userLoginDetails?.authToken ?: ""
                Log.d("LoginViewModel", "Fetched Token from API: $authToken")
                val userData = UserData(email = email, token = authToken, isLogin = true)

                // Store token in UserPreferenceManager immediately
                saveSession(userData) // This calls repository.storeSession(userData)

                _token.value = authToken // Update LiveData for UI
                _isSuccess.value = true
                _messages.value = response.responseMessage
            } catch (e: HttpException) {
                // ... (error handling)
            }
        }
    }
}