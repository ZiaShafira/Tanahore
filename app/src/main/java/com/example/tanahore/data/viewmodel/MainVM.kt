package com.example.tanahore.data.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tanahore.data.response.ArticleResponse
import com.example.tanahore.data.response.ArticlesItem
import com.example.tanahore.data.response.DataItem
import com.example.tanahore.data.response.SearchResponse
import com.example.tanahore.data.retrofit.ConfigApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainVM: ViewModel() {
    private val _dataItem = MutableLiveData<List<DataItem>>()
    val dataItem: LiveData<List<DataItem>> = _dataItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _articlesItem = MutableLiveData<List<ArticlesItem>>()
    val articlesItem: LiveData<List<ArticlesItem>> = _articlesItem

    fun getAllArticles() {
        _isLoading.value = true
        val client = ConfigApi.getApiService().home()
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _dataItem.value = responseBody.data
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchArticle(context: Context, query: String) {
        _isLoading.value = true
        val client = ConfigApi.getApiService().searchArticles(query)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _articlesItem.value = responseBody.data.articles
                        Log.d("MainVM", "dataItem changed: ${_articlesItem.value}")
                    }
                } else {
                    Toast.makeText(context, "Article Not Found", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}