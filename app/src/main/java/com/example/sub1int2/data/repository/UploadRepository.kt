package com.example.sub1int2.data.repository

import androidx.lifecycle.liveData
import com.example.sub1int2.data.ResultState
import com.example.sub1int2.data.api.ApiService
import com.example.sub1int2.data.response.FileUploadResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UploadRepository private constructor(
    private val apiService: ApiService
) {

    fun uploadImage(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(multipartBody, requestBody,)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }

    }

    suspend fun uploadStory(imageFile: MultipartBody.Part, description: RequestBody): FileUploadResponse {
        return apiService.uploadImage(imageFile, description)
    }

    companion object {
        @Volatile
        private var instance: UploadRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UploadRepository(apiService)
            }.also { instance = it }
    }
}