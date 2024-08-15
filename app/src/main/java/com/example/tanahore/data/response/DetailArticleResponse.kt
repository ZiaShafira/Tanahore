package com.example.tanahore.data.response

import com.google.gson.annotations.SerializedName

data class DetailArticleResponse(

    @field:SerializedName("data")
    val data: DataArticle,

    @field:SerializedName("code")
    val status: String,

    @field:SerializedName("message")
    val message: String
)

data class DataArticle(

    @field:SerializedName("imageURL")
    val image: String,

    @field:SerializedName("title")
    val name: String,

    @field:SerializedName("content")
    val description: String,

    @field:SerializedName("articleID")
    val id: Int
)