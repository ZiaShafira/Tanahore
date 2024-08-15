package com.example.tanahore.injection

import android.content.Context
import com.example.tanahore.data.retrofit.ConfigApi
import com.example.tanahore.repository.UserRepository

object Data {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ConfigApi.getApiService()
        return UserRepository(apiService)
    }
}