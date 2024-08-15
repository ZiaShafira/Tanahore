package com.example.tanahore.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tanahore.data.Results
import com.example.tanahore.data.response.IotResponse
import com.example.tanahore.data.response.LoginResponse
import com.example.tanahore.data.response.RegisterResponse
import com.example.tanahore.data.response.ScanResponse
import com.example.tanahore.data.retrofit.ServiceApi
import okhttp3.MultipartBody

class UserRepository (
    private val apiService: ServiceApi
) {
    fun register(
        name: String,
        email: String,
        pass: String
    ): LiveData<Results<RegisterResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.register(name, email, pass)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.d("register", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }

    fun login(email: String, pass: String): LiveData<Results<LoginResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.login(email, pass)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.d("login", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }

    fun postImage(
        file: MultipartBody.Part,
        token: String
    ): LiveData<Results<ScanResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.postImage(file, token)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.d("post_image", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }

    fun postPh(jenisTanah: String, id: String, token: String): LiveData<Results<IotResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.postPh(jenisTanah, id, token)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.d("post_ph", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }
}