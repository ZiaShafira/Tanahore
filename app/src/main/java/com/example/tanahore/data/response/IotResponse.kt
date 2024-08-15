package com.example.tanahore.data.response

import com.google.gson.annotations.SerializedName

data class IotResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: DataIot
)

data class DataIot(
    @SerializedName("data") val data: DataIot2,
    @SerializedName("status") val status: DataIot3
)

data class DataIot2(
    @SerializedName("plantRecommendation") val plantRecommendation: String,
    @SerializedName("suhu") val suhu: Float,
    @SerializedName("kelembapan") val kelembapan: Int,
    @SerializedName("intensitasCahaya") val intensitasCahaya: Int,
    @SerializedName("ph") val ph: Float
)

data class DataIot3(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)
