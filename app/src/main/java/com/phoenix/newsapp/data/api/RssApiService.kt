package com.phoenix.newsapp.data.api

import com.phoenix.newsapp.domain.model.RssChannel
import retrofit2.Call
import retrofit2.http.GET

const val BASE_URL = "https://feedx.net"

interface RssApiService {

    @GET("/rss/ap.xml")
    fun getRssFeed(): Call<RssChannel>
}