package com.example.sub1int2.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sub1int2.data.ResultState
import com.example.sub1int2.data.repository.UploadRepository
import com.example.sub1int2.data.response.FileUploadResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

class AddStoryViewModel(private val repository: UploadRepository) : ViewModel() {
    private val _uploadResult = MutableLiveData<ResultState<FileUploadResponse>>()
    val uploadResult: LiveData<ResultState<FileUploadResponse>> = _uploadResult

    fun uploadStory(imageFile: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            _uploadResult.value = ResultState.Loading
            try {
                val response = repository.uploadStory(imageFile, description)
                _uploadResult.value = ResultState.Success(response)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                _uploadResult.value = ResultState.Error(errorResponse.message ?: "Upload failed")
            } catch (e: Exception) {
                _uploadResult.value = ResultState.Error("Network error occurred")
            }
        }
    }
}