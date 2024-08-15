package com.example.tanahore.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanahore.repository.UserRepository

class RegisterVM (
    private val repository: UserRepository
) : ViewModel(){
    fun register(name: String, email: String, pass: String) = repository.register(name, email, pass)
}