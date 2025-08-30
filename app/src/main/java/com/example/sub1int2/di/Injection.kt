package com.example.sub1int2.di

import android.content.Context
import com.example.sub1int2.data.api.ApiConfig
import com.example.sub1int2.data.repository.UserRepository
import com.example.sub1int2.data.pref.UserPreference
import com.example.sub1int2.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(
            pref,
            ApiConfig.getApiService("")
        )
    }
}