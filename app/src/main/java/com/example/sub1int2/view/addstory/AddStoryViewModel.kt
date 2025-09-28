package com.example.sub1int2.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sub1int2.data.ResultState
import com.example.sub1int2.data.pref.UserModel
import com.example.sub1int2.data.repository.UploadRepository
import com.example.sub1int2.data.repository.UserRepository
import com.example.sub1int2.data.response.FileUploadResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

class AddStoryViewModel(
    private val uploadRepository: UploadRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

//    suspend fun uploadStory(imageFile: MultipartBody.Part, description: RequestBody, token: String) {
//        return uploadRepository.uploadImage(imageFile, description, token)
//    }
}