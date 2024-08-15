package com.example.tanahore.data

import java.io.Serializable

sealed class Results <out R> private constructor() {
    data class Success<out T>(val data: T) : Results<T>(), Serializable
    data class Error(val error: String) : Results<Nothing>(), Serializable
    object Loading : Results<Nothing>()
}