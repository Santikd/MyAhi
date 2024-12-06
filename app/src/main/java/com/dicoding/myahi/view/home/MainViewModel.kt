package com.dicoding.myahi.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.myahi.data.repository.UserRepository
import com.dicoding.myahi.data.preferences.UserData
import com.dicoding.myahi.data.response.ErrorResponse
import com.dicoding.myahi.data.response.ListStoryItem
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: UserRepository): ViewModel() {
    private val _listStories = MutableLiveData<List<ListStoryItem?>>()
    val listStories: LiveData<List<ListStoryItem?>> = _listStories
    fun getSession(): LiveData<UserData> {
        return repository.retrieveSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            repository.signOut()
        }
    }
    fun getStories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _listStories.value = repository.getStories()
                _isLoading.value = false
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.errorMessage
                _messages.value = errorMessage.toString()
                _isLoading.value = false
            }
        }
    }
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _messages = MutableLiveData<String?>()
    val messages: LiveData<String?> = _messages

}