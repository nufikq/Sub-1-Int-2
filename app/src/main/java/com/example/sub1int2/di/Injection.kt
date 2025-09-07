package com.example.sub1int2.di

import android.content.Context
import com.example.sub1int2.data.api.ApiConfig
import com.example.sub1int2.data.repository.UserRepository
import com.example.sub1int2.data.pref.UserPreference
import com.example.sub1int2.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService {
            runBlocking { pref.getSession().first().token }
        }
        return UserRepository.getInstance(
            pref,
            apiService
        )
    }
}