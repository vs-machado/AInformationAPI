package com.phoenix.newsapp.feature_news.data.repository

import com.phoenix.newsapp.feature_news.data.api.RssApiService
import com.phoenix.newsapp.feature_news.domain.model.RssFeed
import com.phoenix.newsapp.feature_news.domain.model.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val rssApiService: RssApiService
): NewsRepository {

    override suspend fun getRssFeed(): RssFeed {
        return rssApiService.getRssFeed()
    }
}