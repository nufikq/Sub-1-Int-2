package com.example.sub1int2.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.sub1int2.data.ResultState
import com.example.sub1int2.data.api.ApiConfig
import com.example.sub1int2.data.repository.UserRepository
import com.example.sub1int2.data.pref.UserModel
import com.example.sub1int2.data.response.FileUploadResponse
import com.example.sub1int2.data.response.ListStoryItem
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> get() = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchStory() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // Get the user session to access the token
                repository.getSession().collect { user ->
                    if (user.isLogin && user.token.isNotEmpty()) {
                        val response = ApiConfig.getApiService { user.token }.getStories()
                        _isLoading.value = false

                        if (response.listStory.isEmpty()) {
                            _stories.value = emptyList()
                        } else {
                            _stories.value = response.listStory
                        }
                    } else {
                        _isLoading.value = false
                        _errorMessage.value = "User not logged in"
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message
            }
        }
    }

//    fun uploadStory(imageFile: MultipartBody.Part, description: RequestBody): LiveData<ResultState<FileUploadResponse>> = liveData {
//        emit(ResultState.Loading)
//        try {
//            val response = repository.uploadStory(imageFile, description)
//            emit(ResultState.Success(response))
//        } catch (e: Exception) {
//            emit(ResultState.Error(e.message ?: "Unknown error"))
//        }
//    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}