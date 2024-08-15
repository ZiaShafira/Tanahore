package com.example.tanahore.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
    val data: DataLogin,

    @field:SerializedName("code")
    val code: String,

    @field:SerializedName("message")
    val message: String,
)

data class DataLogin(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("accessToken")
    val accessToken: String
)