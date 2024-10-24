package com.phoenix.ainformation.feature_news.domain.model.repository

import com.phoenix.ainformation.feature_news.domain.model.RssItem

interface NewsRepository {
    suspend fun getItems(page: Int, pageSize: Int): Result<List<RssItem>>
}