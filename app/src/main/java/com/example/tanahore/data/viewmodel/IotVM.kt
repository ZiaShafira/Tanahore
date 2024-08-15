package com.example.tanahore.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanahore.repository.UserRepository

class IotVM (
    private val repository: UserRepository
    ) : ViewModel() {
    fun postPh(
        jenisTanah: String,
        id: String,
        token: String
    ) = repository.postPh(jenisTanah, id, token)
}