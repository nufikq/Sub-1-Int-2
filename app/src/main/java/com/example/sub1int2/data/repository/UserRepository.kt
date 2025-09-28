package com.example.sub1int2.data.repository

import com.example.sub1int2.data.api.ApiService
import com.example.sub1int2.data.pref.UserModel
import com.example.sub1int2.data.pref.UserPreference
import com.example.sub1int2.data.response.FileUploadResponse
import com.example.sub1int2.data.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun logout() {
        userPreference.logout()
    }

//    suspend fun uploadStory(imageFile: MultipartBody.Part, description: RequestBody): FileUploadResponse {
//        val user = userPreference.getSession().first()
//        return apiService.uploadImage(imageFile, description, "Bearer ${user.token}")
//    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}