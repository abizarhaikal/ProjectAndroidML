package com.dicoding.asclepius.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.response.CancerResponse
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import com.dicoding.asclepius.repositoriy.CancerRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel(): ViewModel() {
    private val _listItem = MutableLiveData<List<ArticlesItem>>()
    val listItem : LiveData<List<ArticlesItem>> = _listItem

    private val _snackbar = MutableLiveData<Boolean>()
    val snackbar : LiveData<Boolean> = _snackbar

    init {
        seeArticle()
    }

    private fun seeArticle() {
        val client = ApiConfig.getApiService().getTopHeadlines(BuildConfig.API_KEY)
        client.enqueue(object: Callback<CancerResponse> {
            override fun onResponse(call: Call<CancerResponse> ,response: Response<CancerResponse>) {
                _snackbar.value = true
                if (response.isSuccessful) {
                    _snackbar.value = false
                    val items = response.body()?.articles
                    _listItem.value = response.body()?.articles

                } else {
                    Log.e(TAG, "OnFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CancerResponse> ,t: Throwable) {
                _snackbar.value = true
                Log.e(TAG, "OnFailure: ${t.message}")
            }
        })
    }

    companion object {
        const val TAG = "CancerResponse"
    }

}