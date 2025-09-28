package com.example.sub1int2.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sub1int2.data.repository.UploadRepository
import com.example.sub1int2.data.repository.UserRepository
import com.example.sub1int2.di.Injection
import com.example.sub1int2.view.addstory.AddStoryViewModel
import com.example.sub1int2.view.login.LoginViewModel
import com.example.sub1int2.view.main.MainViewModel

class ViewModelFactory(
    private val uploadRepository: UploadRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(uploadRepository, userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUploadRepository(),
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}