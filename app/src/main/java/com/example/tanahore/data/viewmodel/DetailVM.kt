package com.example.tanahore.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tanahore.data.response.DataArticle
import com.example.tanahore.data.response.DetailArticleResponse
import com.example.tanahore.data.retrofit.ConfigApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailVM : ViewModel() {
    private val _detail = MutableLiveData<DataArticle>()
    val detail: LiveData<DataArticle> = _detail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun articleDetail(id : Int){
        _isLoading.value = true
        val client = ConfigApi.getApiService().getDetail(id)
        client.enqueue(object : Callback<DetailArticleResponse> {
            override fun onResponse(call: Call<DetailArticleResponse>, response: Response<DetailArticleResponse>) {
                _isLoading.value = false

                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _detail.value = responseBody.data
                    }
                } else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailArticleResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "DetailViewModel"
    }
}