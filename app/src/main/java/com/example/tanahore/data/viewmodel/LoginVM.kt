package com.example.tanahore.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanahore.repository.UserRepository

class LoginVM (
    private val repository: UserRepository

) : ViewModel(){
    fun login(email: String, pass: String) = repository.login(email, pass)
}