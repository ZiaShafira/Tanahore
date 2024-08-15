package com.example.tanahore.data.retrofit

import com.example.tanahore.data.response.ArticleResponse
import com.example.tanahore.data.response.DetailArticleResponse
import com.example.tanahore.data.response.IotResponse
import com.example.tanahore.data.response.LoginResponse
import com.example.tanahore.data.response.RegisterResponse
import com.example.tanahore.data.response.ScanResponse
import com.example.tanahore.data.response.SearchResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ServiceApi {
    @GET("/mobile/api/articles/search/{query}")
    fun searchArticles(
        @Path("query") query: String,
    ): Call<SearchResponse>

    @GET("/mobile/api/articles/{id}")
    fun getDetail(
        @Path("id") id: Int
    ): Call<DetailArticleResponse>

    @GET("/mobile/api/articles")
    fun home(
    ): Call<ArticleResponse>

    @Multipart
    @POST("/mobile/api/predict/soil")
    suspend fun postImage(
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): ScanResponse

    @FormUrlEncoded
    @POST("/mobile/api/device/{id}/predict")
    suspend fun postPh(
        @Path("id")  id: String,
        @Field("jenisTanah") jenisTanah: String,
        @Header("Authorization") token: String
    ): IotResponse

    @FormUrlEncoded
    @POST("/mobile/api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("/mobile/api/register")
    suspend fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse
}