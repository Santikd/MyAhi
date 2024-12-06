package com.dicoding.myahi.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.dicoding.myahi.data.repository.UserRepository
import com.dicoding.myahi.data.response.ErrorResponse
import retrofit2.HttpException

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> get() = _message

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    suspend fun registerUser(name: String, email: String, password: String) {
        try {
            val responseMessage = repository.registerUser(name, email, password)
            _isLoading.value = false
            _isSuccess.value = true
            _message.value = responseMessage
        } catch (exception: HttpException) {
            val errorResponse = exception.response()?.errorBody()?.string()
            val error = Gson().fromJson(errorResponse, ErrorResponse::class.java)
            val errorMessage = error.errorMessage
            _isLoading.value = false
            _isSuccess.value = false
            _message.value = errorMessage.toString()
        }
    }
}
