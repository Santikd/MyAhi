package com.dicoding.myahi.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.myahi.data.repository.UserRepository
import com.dicoding.myahi.data.response.StoryDetail
import com.dicoding.myahi.data.response.ListStoryItem
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loadingStatus = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>()
    val messages: LiveData<String?> = _errorMessage

    private val _storyDetails = MutableLiveData<StoryDetail?>()
    val storyDetails: LiveData<StoryDetail?> = _storyDetails

    fun getStoryDetails(storyId: String) {
        _loadingStatus.value = true
        viewModelScope.launch {
            try {
                val storyData = repository.getDetailStory(storyId)
                _storyDetails.value = storyData
                Log.d(LOG_TAG, "Fetched story details: $storyData")
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(LOG_TAG, "Error fetching story details", e)
            } finally {
                _loadingStatus.value = false
            }
        }
    }

    companion object {
        private const val LOG_TAG = "StoryDetailViewModel"
    }
}
