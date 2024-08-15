package com.example.tanahore.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanahore.repository.UserRepository
import okhttp3.MultipartBody

class CameraVM (
    private val repository: UserRepository
) : ViewModel() {

    fun postImage(
        file: MultipartBody.Part,
        token: String
    ) = repository.postImage(file, token)
}