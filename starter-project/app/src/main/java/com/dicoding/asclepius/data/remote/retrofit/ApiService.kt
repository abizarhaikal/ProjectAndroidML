package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.data.remote.response.CancerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines?q=cancer&category=health&language=en")
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String
    ): Call<CancerResponse>
}