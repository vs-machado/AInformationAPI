package com.phoenix.newsapp.feature_news.data.repository

import com.phoenix.newsapp.feature_news.data.api.RssApiService
import com.phoenix.newsapp.feature_news.domain.model.RssItem
import com.phoenix.newsapp.feature_news.domain.model.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val rssApiService: RssApiService
): NewsRepository {

    // Fetch items from the rssfeed. pageSize defines how many items will be fetched on each method call.
    override suspend fun getItems(page: Int, pageSize: Int): Result<List<RssItem>> {
        return try {
            val rssFeed = rssApiService.getRssFeed()
            val itemsList = rssFeed.channel.items
            val startIndex = page * pageSize

            if(startIndex + pageSize <= itemsList.size) {
                Result.success(
                    itemsList.slice(startIndex until startIndex + pageSize)
                )
            } else Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}