package com.phoenix.newsapp.feature_news.data.api

import com.phoenix.newsapp.feature_news.domain.model.RssFeed
import retrofit2.http.GET

const val BASE_URL = "https://feedx.net"

interface RssApiService {

    @GET("/rss/ap.xml")
    suspend fun getRssFeed(): RssFeed
}