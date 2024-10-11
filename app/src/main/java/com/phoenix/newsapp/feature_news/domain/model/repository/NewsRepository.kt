package com.phoenix.newsapp.feature_news.domain.model.repository

import com.phoenix.newsapp.feature_news.domain.model.RssFeed

interface NewsRepository {
    suspend fun getRssFeed(): RssFeed
}