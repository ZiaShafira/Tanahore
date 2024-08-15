package com.example.tanahore.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tanahore.injection.Data
import com.example.tanahore.repository.UserRepository

class FactoryVM (
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CameraVM::class.java) -> {
                CameraVM(repository) as T
            }
            modelClass.isAssignableFrom(IotVM::class.java) -> {
                IotVM(repository) as T
            }
            modelClass.isAssignableFrom(RegisterVM::class.java) -> {
                RegisterVM(repository) as T
            }
            modelClass.isAssignableFrom(LoginVM::class.java) -> {
                LoginVM(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: FactoryVM? = null
        fun getInstance(context: Context): FactoryVM =
            instance ?: synchronized(this) {
                instance ?: FactoryVM(Data.provideRepository(context))
            }.also {
                instance = it
            }
    }

}