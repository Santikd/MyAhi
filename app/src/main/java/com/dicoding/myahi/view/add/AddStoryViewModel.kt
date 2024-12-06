package com.dicoding.myahi.view.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.dicoding.myahi.data.repository.UserRepository
import com.dicoding.myahi.data.response.ErrorResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean> = _isLoading

    private val _messages = MutableLiveData<String?>()
    val userMessages: LiveData<String?> = _messages

    fun uploadStory(imagePart: MultipartBody.Part, descriptionPart: RequestBody) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.addStory(imagePart, descriptionPart)
                _messages.value = response.errorMessage
                _isLoading.value = false
            } catch (exception: HttpException) {
                val errorBody = exception.response()?.errorBody()?.string()
                val errorData = Gson().fromJson(errorBody, ErrorResponse::class.java)
                _messages.value = errorData.errorMessage
                _isLoading.value = false
            }
        }
    }
}
