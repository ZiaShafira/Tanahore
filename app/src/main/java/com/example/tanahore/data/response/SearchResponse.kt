package com.example.tanahore.data.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(

    @field:SerializedName("code")
    val status: String,

    @field:SerializedName("data")
    val data: DataSearch,

    @field:SerializedName("message")
    val message: String
)
data class DataSearch(

    @field:SerializedName("articles")
    val articles: List<ArticlesItem>
)

data class ArticlesItem(

    @field:SerializedName("imageURL")
    val image: String,

    @field:SerializedName("title")
    val name: String,

    @field:SerializedName("articleID")
    val id: Int
)